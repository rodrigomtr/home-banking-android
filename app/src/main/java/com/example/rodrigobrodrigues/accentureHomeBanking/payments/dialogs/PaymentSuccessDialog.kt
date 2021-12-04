package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment

import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.proving.PaymentProvingActivity

/**
 * Dialog used when a payment succeed
 * @author Rodrigo Rodrigues on 07/02/2018.
 * @see [Dialog Fragment documentation](https://developer.android.com/reference/android/app/DialogFragment.html)
 */
class PaymentSuccessDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.payment_success)
                .setIcon(R.drawable.ic_check_circle_24dp)
                .setPositiveButton(R.string.ok) { _, _ -> dismiss() }
                .create()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        val intent = Intent(context, PaymentProvingActivity::class.java)
        intent.putExtras(arguments)
        startActivity(intent)
    }
}
