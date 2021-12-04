package com.example.rodrigobrodrigues.accentureHomeBanking.payments.textRecognizer

import android.content.Intent

import com.google.android.gms.vision.text.TextRecognizer

/**
 * @author Rodrigo Rodrigues on 23/02/2018.
 */

internal interface TextRecognizerContract {
    interface View {

        fun requestCameraPermission()

        fun checkLowStorage()

        fun createCameraSource()

        fun createCameraSource(autoFocus: Boolean, useFlash: Boolean, textRecognizer: TextRecognizer)

        fun setResult(result: Int, intent: Intent)

        fun finish()

        fun setServiceTarget()

        fun setImpTarget()

        fun startTax()

        fun startService()
    }

    interface Presenter {
        fun startCamera(permission: Int, textRecognizer: TextRecognizer?)

        fun detected(value: String)

        fun getRef(): String?

        fun getAmount(): String?

        fun getEntity(): String?
    }
}
