package com.example.rodrigobrodrigues.accentureHomeBanking.payments.service

import android.widget.EditText

import com.example.rodrigobrodrigues.accentureHomeBanking.payments.BasePaymentContract

/**
 * @author Rodrigo Rodrigues on 26/01/2018.
 */

interface ServicePaymentContract {
    interface View : BasePaymentContract.View {

        fun showLoadingProgressDialog()

        fun dismissProgressDialog()

        fun showFieldsError()

        fun showFailureDialog()

        fun showSuccessDialog()

        fun showEntityError()

        fun showRefError()

        fun showAmountError()

        fun setArguments(entity: String, ref: String, amount: Double, accountId: Long, date: String, result: Int?)

        fun hidePredefinedEntity()

        fun showPredefinedEntity()

        fun showConfirmationDialog(account: Long, entity: String?, ref: String?, amount: String?, period: Int, date: String?)

    }

    interface Presenter : BasePaymentContract.Presenter {

        var period: Int

        fun pay()

        fun validatePayment(entityET: EditText, refET1: EditText, refET2: EditText, refET3: EditText, amountET1: EditText, amountET2: EditText)

        fun setServiceType(i: Int)

        fun serviceTypeId(position: Int): Int

        fun handleServiceType()
    }
}
