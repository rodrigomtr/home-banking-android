package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.support.v4.app.DialogFragment
import android.os.Bundle

import com.example.rodrigobrodrigues.accentureHomeBanking.R

/**
 * Dialog with all payment information
 *
 * @author Rodrigo Rodrigues on 19/02/2018.
 * @see [Dialog Fragment documentation](https://developer.android.com/reference/android/app/DialogFragment.html)
 */

class ConfirmPaymentDialog : DialogFragment() {

    private var listener: ConfirmPaymentDialog.OnPaymentConfirmationListener? = null

    /**
     * Interface definition for a callback to be invoked when positive button is clicked.
     */
    interface OnPaymentConfirmationListener {
        /**
         * Callback method to be invoked when positive button is clicked.
         */
        fun onPaymentConfirmation()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity: Activity

        if (context is Activity) {
            activity = context
            listener = activity as ConfirmPaymentDialog.OnPaymentConfirmationListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val body = arguments.getString("body")
        return AlertDialog.Builder(context)
                .setTitle(getString(R.string.dialog_title))
                .setMessage(body)
                .setPositiveButton(R.string.ok) { _, _ ->
                    listener!!.onPaymentConfirmation()
                    dismiss()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> dismiss() }
                .create()
    }

    companion object {

        fun newInstance(body: String): ConfirmPaymentDialog {
            val args = Bundle()
            args.putString("body", body)
            val fragment = ConfirmPaymentDialog()
            fragment.arguments = args
            return fragment
        }
    }
}
