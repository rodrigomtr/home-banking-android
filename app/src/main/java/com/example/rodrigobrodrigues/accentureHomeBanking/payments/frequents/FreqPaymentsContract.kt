package com.example.rodrigobrodrigues.accentureHomeBanking.payments.frequents

import com.example.rodrigobrodrigues.accentureHomeBanking.models.PaymentSender

/**
 * @author Rodrigo Rodrigues on 06/03/2018.
 */

internal interface FreqPaymentsContract {
    interface View {

        fun showLoadingProgressDialog()

        fun dismissProgressDialog()

        fun showError()

        fun setListView(payments: List<PaymentSender>)

        fun setResult(entity: String, ref: String, amount: String)
    }

    interface Presenter {

        fun itemClicked(itemAtPosition: PaymentSender)

        fun handleFrequents()
    }
}
