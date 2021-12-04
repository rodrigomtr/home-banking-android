package com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment

import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.util.Strings
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

/**
 * @author Rodrigo Rodrigues on 22/03/2018.
 */

class DownloadPdfTask{

    fun download(paymentID: Int, downloadManager: DownloadManager) {
        async(CommonPool) {
            val url = Strings.url + "/pay/get/proving/" + paymentID
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri)
            request.setMimeType("application/pdf")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalFilesDir(App.instance!!.context, Environment.DIRECTORY_DOWNLOADS,
                    App.instance!!.context!!.getString(R.string.payment_prove_file))
            downloadManager.enqueue(request)
        }
    }
}