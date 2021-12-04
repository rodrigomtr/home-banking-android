package com.example.rodrigobrodrigues.accentureHomeBanking.payments.qrCode

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.SurfaceHolder

import com.example.rodrigobrodrigues.accentureHomeBanking.AbstractAsyncActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_qrpayment.*
import kotlinx.android.synthetic.main.content_qrpayment.*

import java.io.IOException

class QRPaymentActivity : AbstractAsyncActivity(), SurfaceHolder.Callback, Detector.Processor<Barcode> {

    private var cameraSource: CameraSource? = null
    private val requestCameraPermissionID = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrpayment)
        setSupportActionBar(toolbar)

        val barcodeDetector = BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(640, 480)
                .build()

        surface_view.holder.addCallback(this)

        barcodeDetector.setProcessor(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            requestCameraPermissionID -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                try {
                    cameraSource!!.start(surface_view.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@QRPaymentActivity,
                    arrayOf(Manifest.permission.CAMERA), requestCameraPermissionID)
            return
        }
        try {
            cameraSource!!.start(surface_view.holder)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {

    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        cameraSource!!.stop()
    }

    override fun release() {

    }

    /**
     *
     * @param detections list of detected bar codes
     */
    override fun receiveDetections(detections: Detector.Detections<Barcode>) {
        val qrCodes = detections.detectedItems
        if (qrCodes.size() != 0) {
            val intent = Intent()
            intent.putExtra("qrCode", qrCodes.valueAt(0).displayValue)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
