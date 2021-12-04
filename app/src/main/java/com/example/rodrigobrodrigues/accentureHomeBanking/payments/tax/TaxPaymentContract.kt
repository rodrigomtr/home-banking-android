package com.example.rodrigobrodrigues.accentureHomeBanking.payments.tax

import com.example.rodrigobrodrigues.accentureHomeBanking.payments.BasePaymentContract

/**
 * Interface definition for taxes payment
 * @author Rodrigo Rodrigues on 26/02/2018.
 */
interface TaxPaymentContract {
    interface View : BasePaymentContract.View {

        /**
         * Shows a loading dialog
         */
        fun showLoadingProgressDialog()

        /**
         * Dismiss the loading dialog
         */
        fun dismissProgressDialog()

        /**
         * Shows success dialog
         */
        fun showSuccessDialog()

        /**
         * Shows failure dialog
         */
        fun showFailureDialog()

        /**
         * Show error on ref EditText
         */
        fun showRefError()

        /**
         * Show error on amount EditText
         */
        fun showAmountError()

        /**
         * Show payment details
         * @param account The paying account
         * @param ref The payment reference
         * @param amount The payment amount
         * @param date The payment date
         */
        fun showConfirmationDialog(account: Long, ref: String?, amount: String?, date: String?)

        /**
         * Show error on error TextView
         */
        fun showFieldsError()

        fun setArguments(entity: String, ref: String, amount: Double?, accountId: Long, date: String, result: Int?)

    }

    interface Presenter : BasePaymentContract.Presenter {
        /**
         *
         */
        fun pay()

        /**
         *
         */
        fun verifyForm(ref: String, amount: String)
    }
}
