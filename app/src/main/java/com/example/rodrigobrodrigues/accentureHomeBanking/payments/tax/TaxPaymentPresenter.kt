package com.example.rodrigobrodrigues.accentureHomeBanking.payments.tax


import android.content.SharedPreferences
import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.BasePaymentPresenter
import com.example.rodrigobrodrigues.accentureHomeBanking.util.CurrencyHandler
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks.PaymentTask
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks.ValidatePaymentTask
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * @author Rodrigo Rodrigues on 26/02/2018.
 */
class TaxPaymentPresenter (private val view: TaxPaymentContract.View, sharedPreferences: SharedPreferences)
    : BasePaymentPresenter(view, sharedPreferences), TaxPaymentContract.Presenter {

    private val entity = "10095"
    private var ref: String? = null
    private var amount: Double? = null

    override fun pay() {
        val period = 0

        val request = (super.account.id.toString() + ":" + entity + ":" + ref
                + ":" + amount + ":" + super.year + "-" + (super.month + 1)
                + "-" + super.day + ":" + period
                + ":" + App.instance!!.user!!.id + ":" + "imp")
        view.showLoadingProgressDialog()
        executePayment(request)
    }

    private fun executePayment(request: String) {
        launch(UI) {
            view.showLoadingProgressDialog()
            val serverResponse = PaymentTask().pay(request)
            view.dismissProgressDialog()
            if (serverResponse != -1) {
                view.setArguments(entity, ref!!, amount, super.account.id,
                        java.sql.Date.valueOf(super.year.toString()
                                + "-" + (super.month + 1) + "-"
                                + super.day).toString(),serverResponse)
                view.showSuccessDialog()
            } else {
                view.showFailureDialog()
            }
        }
    }

    override fun verifyForm(ref: String, amount: String) {
        var isValid = true
        this.ref = ref

        if (ref.length != 15) {
            isValid = false
            view.showRefError()
        }
        try {
            if (amount.toDouble() <= 0) {
                isValid = false
                view.showAmountError()
            }
            this.amount = amount.toDouble()
        } catch (e: NumberFormatException) {
            isValid = false
            view.showAmountError()
        }

        if (isValid) {
            this.ref = ref
            serverValidation("$entity:$ref")
        }
    }

    private fun serverValidation(request: String) {
        launch(UI) {
            view.showLoadingProgressDialog()
            val serverResult = ValidatePaymentTask().validate(request)
            view.dismissProgressDialog()
            if(serverResult) proceedPayment() else showFieldsError()
        }
    }

    private fun proceedPayment() {
        view.showConfirmationDialog(super.account.id, ref, CurrencyHandler.displayCurrency(amount), super.date)
    }

    private fun showFieldsError() {
        view.showFieldsError()
    }
}