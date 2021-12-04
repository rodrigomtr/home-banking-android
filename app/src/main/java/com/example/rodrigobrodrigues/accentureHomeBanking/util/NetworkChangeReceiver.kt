package com.example.rodrigobrodrigues.accentureHomeBanking.util

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

/**
 * @author Rodrigo Rodrigues on 20/01/2018.
 */

class NetworkChangeReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = isConnected(context)

        onNetworkChangeListener?.onChange(isConnected)
    }

    interface OnNetworkChangeListener {
        fun onChange(isConnected: Boolean)
    }

    companion object {
        var onNetworkChangeListener: OnNetworkChangeListener? = null

        fun isConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo?.isConnected ?: false
        }
    }
}