package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.EditText
import android.widget.TextView

import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.R

import java.util.regex.Pattern


/**
 * @author Rodrigo Rodrigues on 08/03/2018.
 */

class EmailProvingDialog : DialogFragment() {
    private var listener: OnPositiveListener? = null

    private var email: EditText? = null
    private var name: EditText? = null
    private var description: EditText? = null
    private var errorTV: TextView? = null

    interface OnPositiveListener {

        fun onSendProving(email: String, name: String, description: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity: Activity

        if (context is Activity) {
            activity = context
            listener = activity as EmailProvingDialog.OnPositiveListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity.layoutInflater
        @SuppressLint("InflateParams") val view = inflater.inflate(R.layout.proving_payment_send_email_dialog, null)
        email = view.findViewById(R.id.et_email)
        name = view.findViewById(R.id.et_name)
        description = view.findViewById(R.id.et_description)
        errorTV = view.findViewById(R.id.tv_error)

        email!!.setText(App.instance!!.user!!.client.email)
        name!!.requestFocus()

        return AlertDialog.Builder(activity)
                .setTitle(R.string.send_email_proving)
                .setView(view)
                .setPositiveButton(R.string.ok) { _, _ ->
                    handleEntries()
                    dismiss()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> dismiss() }
                .create()
    }

    private fun handleEntries() {
        val emailString = email!!.text.toString()
        if (validate(emailString)) {
            listener!!.onSendProving(emailString,
                    name!!.text.toString(),
                    description!!.text.toString())
        } else {
            showEmailError()
        }
    }

    private fun showEmailError() {
        errorTV!!.visibility = View.VISIBLE
        email!!.background = ContextCompat.getDrawable(context, R.color.error)
    }

    companion object {

        private val VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

        private fun validate(emailStr: String): Boolean {
            val matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
            return matcher.find()
        }
    }
}
