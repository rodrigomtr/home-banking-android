package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

import com.example.rodrigobrodrigues.accentureHomeBanking.R

/**
 * Dialog used to choose payment frequency
 * @author Rodrigo Rodrigues on 26/01/2018.
 * @see [Dialog Fragment documentation](https://developer.android.com/reference/android/app/DialogFragment.html)
 */
class PeriodSpinnerFragment : DialogFragment() {

    private var listener: OnSetPeriodListener? = null

    /**
     * Interface definition for a callback to be invoked when a payment frequency is selected
     */
    interface OnSetPeriodListener {
        /**
         * Callback method to be invoked when an item of payment frequency list is selected.
         * @param position The position of the item in payment frequency list
         */
        fun onSetPeriod(position: Int)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity: Activity

        if (context is Activity) {
            activity = context
            listener = activity as OnSetPeriodListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setItems(R.array.period_array) { _, position -> listener!!.onSetPeriod(position) }.create()
    }
}