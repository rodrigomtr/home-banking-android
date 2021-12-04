package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

import com.example.rodrigobrodrigues.accentureHomeBanking.R

/**
 * Dialog used in SSPayment to select a remuneration type
 * @author Rodrigo Rodrigues on 26/02/2018.
 * @see [Dialog Fragment documentation](https://developer.android.com/reference/android/app/DialogFragment.html)
 */
class RemunTypeDialog : DialogFragment() {
    private var listener: RemunTypeDialog.OnSetRemunTypeListener? = null

    /**
     * Interface definition for a callback to be invoked when an remuneration type is selected
     */
    interface OnSetRemunTypeListener {
        /**
         * Callback method to be invoked when an item of payment type list is selected
         * @param position The position of remuneration type item
         */
        fun onSetRemunType(position: Int)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity: Activity

        if (context is Activity) {
            activity = context
            listener = activity as RemunTypeDialog.OnSetRemunTypeListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setItems(R.array.remun_type_array) { _, position -> listener!!.onSetRemunType(position) }.create()
    }
}
