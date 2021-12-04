package com.example.rodrigobrodrigues.accentureHomeBanking.payments.proving

import android.app.DownloadManager


/**
 * @author Rodrigo Rodrigues on 08/03/2018.
 */

internal interface PaymentProvingContract {
    interface View {
        val downloadManager: DownloadManager

        fun showLoadingProgressDialog()

        fun dismissProgressDialog()

        fun showEmailSuccess()

        fun showEmailError()
    }

    interface Presenter {

        fun downloadPdf()

        fun sendEmail(email: String, name: String, description: String)
    }
}
