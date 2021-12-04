package com.example.rodrigobrodrigues.accentureHomeBanking

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication

import com.example.rodrigobrodrigues.accentureHomeBanking.models.User
import com.example.rodrigobrodrigues.accentureHomeBanking.util.NetworkChangeReceiver

/**
 * @author Rodrigo Rodrigues on 21/01/2018.
 */

class App : Application() {

    var user: User? = null
        get() {
            return if (field != null) {
                field
            } else {
                val intent = baseContext.packageManager
                        .getLaunchIntentForPackage(baseContext.packageName)!!
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                null
            }
        }

    var context: Context ?= null

    var sharedPreferences: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        sharedPreferences = applicationContext.getSharedPreferences(
                "com.example.rodrigobrodrigues.accentureHomeBanking", Context.MODE_PRIVATE)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun setNetworkChangeListener(listener: NetworkChangeReceiver.OnNetworkChangeListener?) {
        NetworkChangeReceiver.onNetworkChangeListener = listener
    }


    companion object {

        @SuppressLint("StaticFieldLeak")
        @get:Synchronized
        var instance: App? = null
            private set
    }

}
