package com.example.rodrigobrodrigues.accentureHomeBanking.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log

import com.example.rodrigobrodrigues.accentureHomeBanking.login.LoginActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import android.content.ContentValues.TAG
import android.content.Context
import com.example.rodrigobrodrigues.accentureHomeBanking.R

/**
 * @author Rodrigo Rodrigues on 09/02/2018.
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val title = remoteMessage!!.data["title"]//remoteMessage.getNotification().getTitle();
        val message = remoteMessage.data["body"]//remoteMessage.getNotification().getBody();

        Log.e(TAG, "Mensagem recebida")
        Log.d(TAG, "title: $title message: $message")

        remoteMessage.data["Key-1"]
        Log.d(TAG, "data:" + remoteMessage.data["Key-1"])


        val entity = remoteMessage.data["entity"]
        val ref = remoteMessage.data["ref"]
        val amount = java.lang.Double.parseDouble(remoteMessage.data["amount"])
        val account = Integer.parseInt(remoteMessage.data["account"])
        val date = remoteMessage.data["date"]
        val paymentId = Integer.parseInt(remoteMessage.data["paymentId"])

        // Sets an ID for the notification
        val mNotificationId = 12345

        val resultIntent = Intent(this, LoginActivity::class.java)
        resultIntent.putExtra("NOTIFICATION_ID", mNotificationId)
        resultIntent.putExtra("notification", true)
        resultIntent.putExtra("entity", entity)
        resultIntent.putExtra("ref", ref)
        resultIntent.putExtra("amount", amount)
        resultIntent.putExtra("account", account)
        resultIntent.putExtra("date", date)
        resultIntent.putExtra("paymentId", paymentId)

        val actionPay = PendingIntent.getActivity(this, 1, resultIntent, 0)

        val mBuilder = NotificationCompat.Builder(baseContext, "default")
                .setSmallIcon(R.drawable.ic_euro_symbol_white)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentIntent(actionPay)

        // Gets an instance of the NotificationManager service
        val mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build())

        super.onMessageReceived(remoteMessage)
    }
}
