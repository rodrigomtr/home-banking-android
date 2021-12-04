package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker

import java.util.Calendar
import java.util.Date

/**
 * Date Picker used to choose payment date
 *
 * @author Rodrigo Rodrigues on 26/01/2018.
 * @see [Dialog Fragment documentation](https://developer.android.com/reference/android/app/DialogFragment.html)
 *
 * @see [On Date Set Listener documentation](https://developer.android.com/reference/android/app/DatePickerDialog.OnDateSetListener.html)
 */
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var listener: OnDatePickerListener? = null

    private var day: Int = 0
    private var year: Int = 0
    private var month: Int = 0

    /**
     * Interface definition for a callback to be invoked when a date is picked
     */
    interface OnDatePickerListener {
        /**
         * Callback method to be invoked when a date is picked
         * @param day The picked day
         * @param month The picked month
         * @param year The picked year
         */
        fun onDatePick(day: Int, month: Int, year: Int)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            listener = targetFragment as OnDatePickerListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Calling Fragment must implement OnDatePickerListener")
        }

        this.day = arguments.getInt("day", 0)
        this.month = arguments.getInt("month")
        this.year = arguments.getInt("year")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (day == 0) {
            val c = Calendar.getInstance()
            c.time = Date()
            this.year = c.get(Calendar.YEAR)
            this.month = c.get(Calendar.MONTH)
            this.day = c.get(Calendar.DAY_OF_MONTH)
        }
        val dialog = DatePickerDialog(activity, this, year, month, day)
        dialog.datePicker.minDate = System.currentTimeMillis() - 1000

        return dialog
    }

    override fun onDateSet(datePicker: DatePicker, i: Int, i1: Int, i2: Int) {
        this.year = i
        this.month = i1
        this.day = i2
        listener!!.onDatePick(i2, i1, i)
    }
}
