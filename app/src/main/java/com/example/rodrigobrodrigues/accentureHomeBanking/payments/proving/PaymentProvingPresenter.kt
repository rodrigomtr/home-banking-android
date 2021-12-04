package com.example.rodrigobrodrigues.accentureHomeBanking.payments.proving

import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks.DownloadPdfTask
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks.SendProvingEmailPdfTask
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * @author Rodrigo Rodrigues on 08/03/2018.
 */

class PaymentProvingPresenter internal constructor(private val view: PaymentProvingContract.View, private val paymentId: Int)
    : PaymentProvingContract.Presenter {


    override fun downloadPdf() {
        DownloadPdfTask().download(paymentId, view.downloadManager)
    }

    override fun sendEmail(email: String, name: String, description: String) {
        val client = App.instance!!.user!!.client
        val request = (paymentId.toString() + ":" + email + ":" + name + ":" + description + ":"
                + client.firstName + " " + client.lastName)
        serverRequest(request)
    }

    private fun serverRequest(request: String) {
        launch(UI) {
            view.showLoadingProgressDialog()
            val serverResult = SendProvingEmailPdfTask().send(request)
            view.dismissProgressDialog()
            if(serverResult) view.showEmailSuccess() else view.showEmailError()
        }
    }
}
