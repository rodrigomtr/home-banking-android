package com.example.rodrigobrodrigues.accentureHomeBanking.payments.scheduled

import com.example.rodrigobrodrigues.accentureHomeBanking.models.ScheduledPayment

/**
 * @author Rodrigo Rodrigues on 15/03/2018.
 */

internal interface ScheduledPaymentContract {
    interface View {

        fun showLoadingProgressDialog()

        fun dismissProgressDialog()

        fun showError()

        fun setListView(catalog: List<ScheduledPayment>)

        fun selectFirstItem(view: android.view.View)

        fun deselectItem(view: android.view.View)

        fun cleanSelectingView()

        fun selectItem(view: android.view.View)

        fun updateList(scheduledPayment: ScheduledPayment)

        fun showDeleteFailureDialog()

        fun showInfoDialog(scheduledPayment: ScheduledPayment)
    }

    interface Presenter {

        fun listLongClick(i: Int, view: android.view.View)

        fun listClick(i: Int, view: android.view.View)

        fun delete()
    }
}
