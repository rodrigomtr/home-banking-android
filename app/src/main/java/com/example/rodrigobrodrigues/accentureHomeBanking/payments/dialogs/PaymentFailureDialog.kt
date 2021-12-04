package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.support.v4.app.DialogFragment
import android.os.Bundle

import com.example.rodrigobrodrigues.accentureHomeBanking.R

/**
 * Dialog used when a payment fails
 * @author Rodrigo Rodrigues on 07/02/2018.
 * @see [Dialog Fragment documentation](https://developer.android.com/reference/android/app/DialogFragment.html)
 */
class PaymentFailureDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.error_title)
                .setIcon(R.drawable.ic_error_24dp)
                .setMessage(R.string.payment_error)
                .setPositiveButton(R.string.ok) { _, _ -> dismiss() }
                .create()
    }
}
