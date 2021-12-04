package com.example.rodrigobrodrigues.accentureHomeBanking.payments.scheduled

import android.view.View

import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.models.ScheduledPayment
import com.example.rodrigobrodrigues.accentureHomeBanking.models.ScheduledPaymentCatalog
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks.DeleteScheduledPayment
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks.LoadScheduledTask
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

import java.util.ArrayList

/**
 * @author Rodrigo Rodrigues on 15/03/2018.
 */

class ScheduledPaymentPresenter internal constructor(private val view: ScheduledPaymentContract.View)
    : ScheduledPaymentContract.Presenter {

    private var payments: ScheduledPaymentCatalog? = null
    private val listSelectedItems: MutableList<ScheduledPayment>
    private var isSelected: Boolean = false

    init {
        loadScheduledPayments()
        listSelectedItems = ArrayList()
    }

    private fun loadScheduledPayments() {
        launch(UI) {
            view.showLoadingProgressDialog()
            val serverResult = LoadScheduledTask().load(App.instance!!.user!!.id)
            view.dismissProgressDialog()
            if (serverResult != null) handleResult(serverResult) else view.showError()
        }
    }

    override fun listLongClick(i: Int, view: View) {
        if (!isSelected) {
            isSelected = true
            listSelectedItems.add(payments!!.catalog[i])
            this.view.selectFirstItem(view)
        } else {
            listClick(i, view)
        }
    }

    override fun listClick(i: Int, view: View) {
        if (isSelected) {
            if (listSelectedItems.remove(payments!!.catalog[i])) {
                this.view.deselectItem(view)
                if (listSelectedItems.isEmpty()) {
                    this.view.cleanSelectingView()
                    isSelected = false
                }
            } else {
                listSelectedItems.add(payments!!.catalog[i])
                this.view.selectItem(view)
            }
        } else {
            this.view.showInfoDialog(payments!!.catalog[i])
        }
    }

    override fun delete() {
        launch(UI){
            view.showLoadingProgressDialog()
            val serverResult= DeleteScheduledPayment().delete(listSelectedItems)
            view.dismissProgressDialog()
            if (serverResult) updateList() else view.showDeleteFailureDialog()
        }
    }

    private fun updateList() {
        payments!!.catalog.removeAll(listSelectedItems)
        for (sp in listSelectedItems) {
            view.updateList(sp)
        }
        listSelectedItems.clear()
        view.cleanSelectingView()
    }

    private fun handleResult(result: ScheduledPaymentCatalog?) {
        this.payments = result
        view.setListView(this.payments!!.catalog)
    }
}
