package com.example.rodrigobrodrigues.accentureHomeBanking.payments.service

import android.content.SharedPreferences
import android.widget.EditText

import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.BasePaymentPresenter
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks.ValidatePaymentTask
import com.example.rodrigobrodrigues.accentureHomeBanking.util.CurrencyHandler
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks.PaymentTask
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * @author Rodrigo Rodrigues on 26/01/2018.
 */

class ServicePaymentPresenter (private val view: ServicePaymentContract.View, sharedPreferences: SharedPreferences)
    : BasePaymentPresenter(view, sharedPreferences), ServicePaymentContract.Presenter {

    override var period: Int = 0
    private var entity: String? = null
    private var ref: String? = null
    private var amount: Double = 0.0
    private var preDefinedEntity: Boolean = false
    private var serviceType: Int = 0

    override fun setServiceType(i: Int) {
        this.serviceType = i
        when (i) {
            0 -> entity = "20158"
            1 -> entity = "10297"
            2 -> entity = "20442"
        }
    }

    override fun serviceTypeId(position: Int): Int {
        when (serviceType) {
            0 -> {
                when (position) {
                    0 -> entity = "20158"//Ficticia
                    1 -> entity = "10258" //Ficticia
                    2 -> entity = "12158" // Ficticia
                }
                return R.array.internet_array
            }
            1 -> {
                when (position) {
                    0 -> entity = "10297" //Vodafone
                    1 -> entity = "21159" //MEO
                    2 -> entity = "20442" //NOS
                }
                return R.array.Tele_array
            }
            else -> {
                entity = "21158"//Ficticia
                return R.array.transp_array
            }
        }
    }

    override fun handleServiceType() {
        if (preDefinedEntity) {
            preDefinedEntity = false
            view.hidePredefinedEntity()
        } else {
            preDefinedEntity = true
            view.showPredefinedEntity()
        }
    }

    override fun validatePayment(entityET: EditText, refET1: EditText, refET2: EditText, refET3: EditText, amountET1: EditText, amountET2: EditText) {
        var toValidate = true
        if (!preDefinedEntity) {
            val sEntity = entityET.text.toString()
            if (sEntity.length != 5) {
                view.showEntityError()
                toValidate = false
            } else {
                this.entity = sEntity
            }
        }
        val sRef = (refET1.text.toString()
                + refET2.text.toString()
                + refET3.text.toString())
        val sEuros = amountET1.text.toString()
        val sCents = amountET2.text.toString()

        if (sRef.length < 9) {
            view.showRefError()
            toValidate = false
        }
        if (sEuros.isEmpty() && sCents.isEmpty() && ((sEuros.toInt() + sCents.toInt()) <= 0)) {
            view.showAmountError()
            toValidate = false
        }

        if (toValidate) {
            this.ref = sRef
            this.amount = java.lang.Double.parseDouble("$sEuros.$sCents")
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

    override fun pay() {
        val request = (super.account.id.toString() + ":" + entity + ":" + ref
                + ":" + amount + ":" + super.year + "-" + (super.month + 1)
                + "-" + super.day + ":" + period
                + ":" + App.instance?.user!!.id + ":service")
        executePayment(request)
    }

    private fun executePayment(request: String) {
        launch(UI) {
            view.showLoadingProgressDialog()
            val serverResponse = PaymentTask().pay(request)
            view.dismissProgressDialog()
            if (serverResponse != -1) {
                view.setArguments(entity!!,
                        ref!!,
                        amount,
                        super.account.id,
                        java.sql.Date.valueOf(super.year.toString()
                                + "-" + (super.month + 1)
                                + "-" + super.day).toString(), serverResponse)
                view.showSuccessDialog()
            } else {
                view.showFailureDialog()
            }
        }
    }

    private fun showFieldsError() {
        view.showFieldsError()
    }

    private fun proceedPayment() {
        view.showConfirmationDialog(super.account.id, entity, ref,
                CurrencyHandler.displayCurrency(amount), period, date)
    }
}
