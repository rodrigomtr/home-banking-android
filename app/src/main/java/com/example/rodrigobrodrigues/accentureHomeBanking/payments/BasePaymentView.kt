package com.example.rodrigobrodrigues.accentureHomeBanking.payments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

import com.example.rodrigobrodrigues.accentureHomeBanking.AbstractAsyncActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.dialogs.FailureDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.dialogs.SuccessDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.components.account.AccountView
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.components.date.DateView
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.ConfirmPaymentDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.OperatorListDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.PaymentFailureDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.PaymentSuccessDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.PeriodSpinnerFragment
import com.example.rodrigobrodrigues.accentureHomeBanking.transactionAuth.TransactionAuthDialogFragment
import com.example.rodrigobrodrigues.accentureHomeBanking.util.DateHandler

@SuppressLint("Registered")
open class BasePaymentView
    : AbstractAsyncActivity(),
        BasePaymentContract.View,
        ConfirmPaymentDialog.OnPaymentConfirmationListener,
        DateView.OnDateUpdateListener,
        AccountView.OnAccountSetListener {

    override fun getDateString(day: Int, month: Int, year: Int): String {
        val connector = getString(DateHandler.getConnectorID(day))
        val monthString = getString(DateHandler.getMonthID(month))
        val yearConnect = getString(R.string.date_connect_2)
        return "$day$connector$monthString$yearConnect$year"
    }

    override fun onAccountUpdate() {

    }

    override fun onDatePicker(day: Int, month: Int, year: Int) {

    }

    protected fun setETTextChange(current: EditText, next: EditText) {
        current.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length == current.maxEms)
                    next.requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    fun showOperatorDialog(id: Int) {
        val oDialog = OperatorListDialog()
        val bundle = Bundle()
        bundle.putInt("ID", id)
        oDialog.arguments = bundle
        oDialog.show(supportFragmentManager, "PAYMENTS")
    }

    fun showPeriodDialog() {
        val pDialog = PeriodSpinnerFragment()
        pDialog.show(supportFragmentManager, "PAYMENTS")
    }

    private fun showTransactionAuthDialog() {
        val frag = TransactionAuthDialogFragment()
        frag.show(supportFragmentManager, "TRANSACTION_AUTH")
    }

    fun showConfirmationDialog(message: String) {
        val confirmDialog = ConfirmPaymentDialog.newInstance(message)
        confirmDialog.show(supportFragmentManager, "PAYMENT_CONFIRMATION")
    }

    fun showSuccessDialog() {
        val frag = PaymentSuccessDialog()
        frag.arguments = intent.extras
        frag.show(supportFragmentManager, "PAYMENT_SUCCESS")
    }

    fun showSuccessDialog(title: String, body: String) {
        val dialog = SuccessDialog.newInstance(title, body)
        dialog.show(supportFragmentManager, "EMAIL_PROVING")
    }

    fun showFailureDialog() {
        val frag = PaymentFailureDialog()
        frag.show(supportFragmentManager, "PAYMENT_ERROR")
    }

    fun showFailureDialog(title: String, body: String) {
        val dialog = FailureDialog.newInstance(title, body)
        dialog.show(supportFragmentManager, "EMAIL_PROVING")
    }

    override fun onPaymentConfirmation() {
        showTransactionAuthDialog()
    }
}
