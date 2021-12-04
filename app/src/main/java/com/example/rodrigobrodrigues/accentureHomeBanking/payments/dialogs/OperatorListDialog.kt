package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

import com.example.rodrigobrodrigues.accentureHomeBanking.R

/**
 * Dialog used in ServicePayment to pick a pre-set entity
 * @author Rodrigo Rodrigues on 20/02/2018.
 * @see [Dialog Fragment documentation](https://developer.android.com/reference/android/app/DialogFragment.html)
 */
class OperatorListDialog : DialogFragment() {
    private var listener: OperatorListDialog.OnSetOperatorListener? = null

    /**
     * Interface definition for a callback to be invoked when an operator is selected
     */
    interface OnSetOperatorListener {
        /**
         * Callback method to be invoked when an item of operator list is selected
         * @param position The position of operator item
         */
        fun onSetOperator(position: Int)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity: Activity

        if (context is Activity) {
            activity = context
            listener = activity as OperatorListDialog.OnSetOperatorListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val id = arguments.getInt("ID")
        val array = when (id) {
            2 -> resources.getStringArray(R.array.internet_array)
            0 -> resources.getStringArray(R.array.Tele_array)
            4 -> resources.getStringArray(R.array.transp_array)
            else -> resources.getStringArray(R.array.internet_array)
        }
        val builder = AlertDialog.Builder(activity)
        builder.setItems(array) { _, position -> listener!!.onSetOperator(position) }
        return builder.create()
    }
}// Required empty public constructor
