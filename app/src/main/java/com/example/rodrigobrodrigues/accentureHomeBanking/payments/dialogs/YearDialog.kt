package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

import com.example.rodrigobrodrigues.accentureHomeBanking.R

/**
 * Dialog used in SSPayment to pick a year
 * @author Rodrigo Rodrigues on 26/02/2018.
 * @see [Dialog Fragment documentation](https://developer.android.com/reference/android/app/DialogFragment.html)
 */

class YearDialog : DialogFragment() {

    private var listener: YearDialog.OnSetYearListener? = null

    /**
     * Interface definition for a callback to be invoked when a year is picked
     */
    interface OnSetYearListener {
        /**
         * Callback method to be invoked when a year is picked
         * @param position The position of year item
         */
        fun onSetYear(position: Int)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity: Activity

        if (context is Activity) {
            activity = context
            listener = activity as YearDialog.OnSetYearListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setItems(R.array.year_array) { _, position -> listener!!.onSetYear(position) }.create()
    }
}
