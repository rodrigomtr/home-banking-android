package com.example.rodrigobrodrigues.accentureHomeBanking.offline

import android.os.Bundle

import com.example.rodrigobrodrigues.accentureHomeBanking.AbstractAsyncActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import kotlinx.android.synthetic.main.content_offline.*

/**
 * This Activity is called when user is not connected to internet
 * it just shows a pulse view and internet needed information
 */
class OfflineActivity : AbstractAsyncActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_offline)

        pulseView.startPulse()
    }

    override fun onBackPressed() {
        //Vazio evita que o utilizador volte para a actividade anterior
    }

    override fun onChange(isConnected: Boolean) {
        if (isConnected) {
            finish()
        }
    }
}
