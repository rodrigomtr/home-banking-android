package com.example.rodrigobrodrigues.accentureHomeBanking.payments.textRecognizer

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log

import com.google.android.gms.vision.text.TextRecognizer

import android.content.ContentValues.TAG
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock

/**
 * @author Rodrigo Rodrigues on 23/02/2018.
 */

class TextRecognizerPresenter internal constructor(private val view: TextRecognizerContract.View) : TextRecognizerContract.Presenter, Detector.Processor<TextBlock> {

    private var entity: String? = null
    private var ref: String? = null
    private var amount: String? = null
    private var parent: String? = null


    override fun startCamera(permission: Int, textRecognizer: TextRecognizer?) {

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.

        if (permission == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(textRecognizer)
        } else {
            view.requestCameraPermission()
        }
    }


    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the ocr detector to detect small text samples
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    internal fun createCameraSource(textRecognizer: TextRecognizer?) {

        // Check if the TextRecognizer is operational.
        if (!textRecognizer!!.isOperational) {
            Log.w(TAG, "Detector dependencies are not yet available.")

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            view.checkLowStorage()
        }

        // Create the mCameraSource using the TextRecognizer.
        view.createCameraSource()
    }

    override fun detected(value: String) {
        if (parent == "SERVICE")
            findData(value)
        else if (parent == "IMP")
            findImpData(value)
    }

    private fun findImpData(detect: String) {
        val lines = detect.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (lines.size == 4) {
            val possibleRef = handleString(lines[1])
            val possibleAmount = handleString(lines[3])
            if(possibleRef.matches("(\\d{3}[ ]?){4}\\d{3}".toRegex())
                    && possibleAmount.matches("€? ?\\d+[,|.]\\d{2} ?€?".toRegex())){
                ref = handleString(lines[1])
                amount = handleString(lines[3])
                verifyImpValues()
            }
        }
    }

    private fun verifyImpValues() {
        if (amount != null && ref != null) {
            view.startTax()
        }
    }

    private fun findData(detect: String) {
        val lines = detect.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (lines.size >= 3) {
            val possibleEntity = handleString(lines[0])
            val possibleRef = handleString(lines[1])
            val possibleAmount = handleString(lines[2])
            if(possibleEntity.matches("\\d{5}\$".toRegex())) entity = possibleEntity
            if(possibleRef.matches("(\\d{3}[ ]?){2}\\d{3}".toRegex())) ref = possibleRef
            if(possibleAmount.matches("€? ?\\d+[,|.]\\d{2} ?€?".toRegex())) amount = possibleAmount
            verifyValues()
        } else {
            val possibleDetection = handleString(detect)
            when {
                possibleDetection.matches("\\d{5}\$".toRegex()) -> entity = possibleDetection
                possibleDetection.matches("(\\d{3}[ ]?){2}\\d{3}".toRegex()) -> ref = possibleDetection
                possibleDetection.matches("€? ?\\d+[,|.]\\d{2} ?€?".toRegex()) -> amount = possibleDetection
                else -> return
            }
            verifyValues()
        }
    }

    private fun verifyValues() {
        if (entity != null && ref != null && amount != null) view.startService()
    }

    private fun handleString(s: String): String {
        val number = s.replace("[^\\d.,]+".toRegex(), "")
        return number.replace(",", ".")
    }

    fun setParent(parent: String) {
        this.parent = parent
        if (parent == "SERVICE") {
            view.setServiceTarget()
        } else if (parent == "IMP") {
            view.setImpTarget()
        }
    }

    override fun getRef(): String? {
        return ref
    }

    override fun getAmount(): String? {
        return amount
    }

    override fun getEntity(): String? {
        return entity
    }

    override fun release() {

    }

    override fun receiveDetections(p0: Detector.Detections<TextBlock>?) {
        val items = p0!!.detectedItems
        for (i in 0 until items.size()) {
            val item = items.valueAt(i)
            if (item != null && item.value != null) {
                detected(item.value)
                Log.d("DETECTION", item.value)
            }
        }
    }
}
