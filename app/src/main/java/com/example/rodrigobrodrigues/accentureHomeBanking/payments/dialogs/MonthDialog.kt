package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.R

/**
 * Dialog used in SSPayment to pick a month
 * @author Rodrigo Rodrigues on 26/02/2018.
 * @see [Dialog Fragment documentation](https://developer.android.com/reference/android/app/DialogFragment.html)
 */
class MonthDialog : DialogFragment() {
    private var listener: MonthDialog.OnSetMonthListener? = null

    /**
     * Interface definition for a callback to be invoked when a month is picked
     */
    interface OnSetMonthListener {
        /**
         * Callback method to be invoked when a month is picked
         * @param position The position of month item
         */
        fun onSetMonth(position: Int)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity: Activity

        if (context is Activity) {
            activity = context
            listener = activity as MonthDialog.OnSetMonthListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setItems(R.array.month_array) { _, position -> listener!!.onSetMonth(position) }
        return builder.create()
    }

}// Required empty public constructor
