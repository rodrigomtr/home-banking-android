package com.example.rodrigobrodrigues.accentureHomeBanking.payments.textRecognizer

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast

import com.example.rodrigobrodrigues.accentureHomeBanking.AbstractAsyncActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.service.ServicePaymentActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tax.TaxPaymentActivity
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.activity_text_recognizer.*
import kotlinx.android.synthetic.main.content_text_recognizer.*

import java.io.IOException

class TextRecognizerActivity : AbstractAsyncActivity(), TextRecognizerContract.View, SurfaceHolder.Callback {

    private var mCameraSource: CameraSource? = null

    private var presenter: TextRecognizerPresenter? = null

    private var textRecognizer: TextRecognizer? = null

    private val requestCameraPermissionID = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognizer)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val parent = intent.getStringExtra("PARENT")

        presenter = TextRecognizerPresenter(this)

        textRecognizer = TextRecognizer.Builder(context).build()
        textRecognizer!!.setProcessor(presenter!!)

        presenter!!.setParent(parent)
        presenter!!.startCamera(ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA), textRecognizer)

        surface_view.holder.addCallback(this)


        Snackbar.make(surface_view, R.string.point_to_bill,
                Snackbar.LENGTH_INDEFINITE)
                .show()
    }

    override fun requestCameraPermission() {
        Log.w("TEXT_RECOGNIZER", "Camera permission is not granted. Requesting permission")

        val permissions = arrayOf(Manifest.permission.CAMERA)

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, recHandleCameraPerm)
            return
        }

        val thisActivity = this

        val listener =
                View.OnClickListener {
            ActivityCompat.requestPermissions(thisActivity, permissions,
                    recHandleCameraPerm)
        }

         Snackbar.make(surface_view, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show()
    }

    override fun checkLowStorage() {
        if (!cacheDir.canWrite()) {
            Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show()
            Log.w(tag, getString(R.string.low_storage_error))
        }
    }

    override fun createCameraSource() {
        mCameraSource = CameraSource.Builder(this, textRecognizer)
                .setAutoFocusEnabled(true)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(15.0f)
                .build()
    }

    override fun createCameraSource(autoFocus: Boolean, useFlash: Boolean, textRecognizer: TextRecognizer) {
        mCameraSource = CameraSource.Builder(this, textRecognizer)
                .setAutoFocusEnabled(true)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(15.0f)
                .build()
    }

    override fun setServiceTarget() {
        findViewById<View>(R.id.imp_target).visibility = View.GONE
        findViewById<View>(R.id.mutlibanco_target).visibility = View.VISIBLE
    }

    override fun setImpTarget() {
        findViewById<View>(R.id.imp_target).visibility = View.VISIBLE
        findViewById<View>(R.id.mutlibanco_target).visibility = View.GONE
    }

    /**
     * Restarts the camera.
     */
    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    @Throws(SecurityException::class)
    private fun startCameraSource() {
        // check that the device has play services available.
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                this)
        if (code != ConnectionResult.SUCCESS) {
            val dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, rcHandleGMS)
            dlg.show()
        }
    }

    /**
     * Stops the camera.
     */
    override fun onPause() {
        super.onPause()
        mCameraSource?.stop()
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on [.requestPermissions].
     *
     *
     * **Note:** It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     *
     *
     * @param requestCode  The request code passed in [.requestPermissions].
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either [PackageManager.PERMISSION_GRANTED]
     * or [PackageManager.PERMISSION_DENIED]. Never null.
     * @see .requestPermissions
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode != recHandleCameraPerm) {
            Log.d(tag, "Got unexpected permission result: $requestCode")
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(tag, "Camera permission granted - initialize the camera source")
            // We have permission, so create the camerasource
            presenter!!.createCameraSource(textRecognizer)
            return
        }

        Log.e(tag, "Permission not granted: results len = " + grantResults.size +
                " Result code = " + if (grantResults.isNotEmpty()) grantResults[0] else "(empty)")

        val listener = DialogInterface.OnClickListener { _, _ -> finish() }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show()
    }

        private val tag = "TextRecognizerActivity"

        // Intent request code to handle updating play services if needed.
        private val rcHandleGMS = 9001

        // Permission request codes need to be < 256
        private val recHandleCameraPerm = 2

    override fun startTax() {
        val intent = Intent(context, TaxPaymentActivity::class.java)
        intent.putExtra("ref", presenter!!.getRef())
        intent.putExtra("amount", presenter!!.getAmount())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun startService() {
        val intent = Intent(context, ServicePaymentActivity::class.java)
        intent.putExtra("entity", presenter!!.getEntity())
        intent.putExtra("ref", presenter!!.getRef())
        intent.putExtra("amount", presenter!!.getAmount())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), requestCameraPermissionID)
            return
        }
        try {
            mCameraSource!!.start(surface_view.holder)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Snackbar.make(surface_view, R.string.point_to_bill,
                Snackbar.LENGTH_INDEFINITE)
                .show()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        mCameraSource!!.stop()
    }

    override fun onBackPressed() {
        finish()
    }
}
