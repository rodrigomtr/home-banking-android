package com.example.rodrigobrodrigues.accentureHomeBanking.notifications

import android.content.ContentValues.TAG
import android.util.Log
import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * @author Rodrigo Rodrigues on 09/02/2018.
 */

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        val sharedPreferences = App.instance?.sharedPreferences
        sharedPreferences?.edit()?.putString("notificationToken", refreshedToken)?.apply()
        sharedPreferences?.edit()?.putBoolean("isRefreshed", true)?.apply()

    }
}
