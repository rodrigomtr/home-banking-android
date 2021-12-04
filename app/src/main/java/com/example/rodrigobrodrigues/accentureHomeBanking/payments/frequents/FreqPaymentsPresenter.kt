package com.example.rodrigobrodrigues.accentureHomeBanking.payments.frequents

import android.content.SharedPreferences

import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.models.PaymentSender
import com.example.rodrigobrodrigues.accentureHomeBanking.models.PaymentSenderCatalog
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks.FrequentPaymentsTask
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


/**
 * @author Rodrigo Rodrigues on 06/03/2018.
 */
internal class FreqPaymentsPresenter(private val view: FreqPaymentsContract.View,
                                     private val parent: String,
                                     private val sharedPreferences: SharedPreferences)
    : FreqPaymentsContract.Presenter{

    private val accountId: Long
        get() {
            return App.instance!!
                    .user!!
                    .client
                    .accounts[sharedPreferences.getInt("account", 0)].id
        }

    init {
        handleFrequents()
    }

    private fun handleImpFrequents() {
        requestFrequents(accountId.toString() + ":imp")
    }

    private fun handleServiceFrequents() {
        requestFrequents(accountId.toString() + ":service")
    }

    private fun requestFrequents(request: String) {
        launch(UI) {
            view.showLoadingProgressDialog()
            val serverResponse = FrequentPaymentsTask().frequentPayments(request)
            view.dismissProgressDialog()
            if (serverResponse != null && serverResponse.catalog.size > 0)
                handleResult(serverResponse)
            else
                view.showError()
        }
    }

    override fun itemClicked(itemAtPosition: PaymentSender) {
        view.setResult(itemAtPosition.entity,
                itemAtPosition.ref,
                itemAtPosition.amount.toString())
    }

    override fun handleFrequents() {
        when (parent) {
            "SERVICE" -> handleServiceFrequents()
            "IMP" -> handleImpFrequents()
        }
    }

    private fun handleResult(result: PaymentSenderCatalog) {
        view.setListView(result.catalog)
    }
}
