package com.example.rodrigobrodrigues.accentureHomeBanking

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.EditText

import com.example.rodrigobrodrigues.accentureHomeBanking.offline.OfflineActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.util.NetworkChangeReceiver
import com.example.rodrigobrodrigues.accentureHomeBanking.dialogs.ProgressDialog


abstract class AbstractAsyncActivity
    : AppCompatActivity(), NetworkChangeReceiver.OnNetworkChangeListener {

    private var progressDialog: ProgressDialog? = null

    private var destroyed = false

    private val networkRequest = 100

    val context: Context
        get() = applicationContext

    override fun onDestroy() {
        super.onDestroy()
        destroyed = true
    }

    override fun onResume() {
        super.onResume()
        App.instance?.setNetworkChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        App.instance?.setNetworkChangeListener(null)
    }

    @SuppressLint("InflateParams")
    fun showLoadingProgressDialog() {
        progressDialog = ProgressDialog()
        progressDialog!!.show(fragmentManager, "PROGRESS")
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    fun checkConnection() {
        if (!NetworkChangeReceiver.isConnected(this))
            showOfflineView()
    }

    override fun onChange(isConnected: Boolean) {
        if (!isConnected)
            showOfflineView()
    }

    open fun onSuccessfulAuth() {

    }

    private fun showOfflineView() {
        startActivityForResult(Intent(this, OfflineActivity::class.java), networkRequest)
    }

    fun setErrorET(editText: EditText) {
        editText.background.setColorFilter(ContextCompat.getColor(context, R.color.error),
                PorterDuff.Mode.SRC_ATOP)
    }

    fun setDefaultET(editText: EditText) {
        editText.background.colorFilter = null
    }
}

