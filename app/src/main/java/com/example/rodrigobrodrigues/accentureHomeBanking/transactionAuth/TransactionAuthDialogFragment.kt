package com.example.rodrigobrodrigues.accentureHomeBanking.transactionAuth


import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText

import com.example.rodrigobrodrigues.accentureHomeBanking.AbstractAsyncActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import kotlinx.android.synthetic.main.auth_dialog_content.*
import kotlinx.android.synthetic.main.auth_dialog_matrix.*
import kotlinx.android.synthetic.main.auth_dialog_matrix.view.*
import kotlinx.android.synthetic.main.fragment_transaction_auth_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class TransactionAuthDialogFragment : DialogFragment(), TransactionAuthContract.View {

    private var presenter: TransactionAuthContract.Presenter? = null
    private var activity: AbstractAsyncActivity? = null

    override val password: String
        get() = (et_val_1.text.toString()
                + et_val_2.text.toString()
                + et_val_3.text.toString()
                + et_val_4.text.toString())

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = getActivity() as AbstractAsyncActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        retainInstance = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dialog.setTitle(R.string.auth)
        return inflater!!.inflate(R.layout.fragment_transaction_auth_dialog, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        ok_dialog_button.setOnClickListener { presenter!!.onPositiveButtonClicked() }
        cancel_button.setOnClickListener { dismiss() }

        setPasswordTextChanged(et_val_1, et_val_2)
        setPasswordTextChanged(et_val_2, et_val_3)
        setPasswordTextChanged(et_val_3, et_val_4)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.presenter = TransactionAuthPresenter(this@TransactionAuthDialogFragment,
                    context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager,
                    context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager)
        } else {
            this.presenter = SimpleTransactionAuthPresenter(this@TransactionAuthDialogFragment)
        }
    }

    private fun setPasswordTextChanged(current: EditText, next: EditText?) {
        current.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length == 1) {
                    current.postDelayed({ next!!.requestFocus() }, 300)
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    override fun setFingerPrintView() {
        fingerprint_container.visibility = View.VISIBLE
        matrix_container.visibility = View.GONE
        ok_dialog_button.setText(R.string.use_password)
    }

    override fun setMatrixView() {
        matrix_container.visibility = View.VISIBLE
        fingerprint_container.visibility = View.GONE
        ok_dialog_button.setText(R.string.ok)
        if (dialog.window != null) {
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    override fun requestPassword(req1: String, req2: String, req3: String, req4: String) {
        matrix_container.tv_request_1.text = req1
        matrix_container.tv_request_2.text = req2
        matrix_container.tv_request_3.text = req3
        matrix_container.tv_request_4.text = req4
    }

    override fun setFingerPrintError() {
        //buttonPanel.visibility = View.GONE
        fp_icon.setImageResource(R.drawable.ic_error_24dp)
        fp_icon.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        status_failure_fp.setTextColor(ContextCompat.getColor(context, R.color.error))
        status_failure_fp.text = context.getString(R.string.error_fingerprint)
        status_failure_fp.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        fp_icon.postDelayed({
            //buttonPanel.visibility = View.VISIBLE
            fp_icon.setImageResource(R.drawable.ic_finger_print)
            fp_icon.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
            status_failure_fp.setText(R.string.fingerprint_hint)
            status_failure_fp.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            status_failure_fp.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        }, delayMillis)
    }

    override fun fingerPrintSuccess() {
        fp_icon.setImageResource(R.drawable.ic_check_circle_24dp)
        status_failure_fp.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        status_failure_fp.text = context.resources.getString(R.string.fingerprint_success)
        fp_icon.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        status_failure_fp.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        fp_icon.postDelayed({
            activity!!.onSuccessfulAuth()
            dismiss()
        }, delayMillis)
    }

    override fun matrixSuccess() {
        matrix_success.visibility = View.VISIBLE
        matrix_request.visibility = View.GONE
        buttonPanel.visibility = View.GONE
        matrix_success.postDelayed({
            activity!!.onSuccessfulAuth()
            dismiss()
        }, delayMillis)
    }

    override fun matrixError() {
        matrix_request.visibility = View.GONE
        matrix_error.visibility = View.VISIBLE
        buttonPanel.visibility = View.GONE
        presenter!!.startMatrixAuth()
        et_val_1.requestFocus()
        et_val_1.text.clear()
        et_val_2.text.clear()
        et_val_3.text.clear()
        et_val_4.text.clear()
        matrix_error.postDelayed({
            matrix_error.visibility = View.GONE
            matrix_request.visibility = View.VISIBLE
            buttonPanel.visibility = View.VISIBLE
        }, delayMillis)
    }

    override fun showLoadingProgressDialog() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissProgressDialog() {
        loading.visibility = View.GONE
    }

    override val checkFPPermission: Boolean
        @RequiresApi(Build.VERSION_CODES.M)
        get() = ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED


    private val delayMillis: Long = 1700
}