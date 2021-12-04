package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

import com.example.rodrigobrodrigues.accentureHomeBanking.R

/**
 * Dialog used in SSPayment to select a payment type
 * @author Rodrigo Rodrigues on 26/02/2018.
 * @see [Dialog Fragment documentation](https://developer.android.com/reference/android/app/DialogFragment.html)
 */
@SuppressLint("ValidFragment")
class PaymentTypeDialog : DialogFragment() {
    private var listener: PaymentTypeDialog.OnSetPaymentTypeListener? = null

    /**
     * Interface definition for a callback to be invoked when an payment type is selected
     */
    interface OnSetPaymentTypeListener {
        /**
         * Callback method to be invoked when an item of payment type list is selected
         * @param position The position of payment type item
         */
        fun onSetPaymentType(position: Int)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity: Activity

        if (context is Activity) {
            activity = context
            listener = activity as PaymentTypeDialog.OnSetPaymentTypeListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setItems(R.array.payment_type_array) { _, position -> listener!!.onSetPaymentType(position) }.create()
    }
}
