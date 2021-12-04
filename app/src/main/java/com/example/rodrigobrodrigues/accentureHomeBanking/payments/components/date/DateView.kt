package com.example.rodrigobrodrigues.accentureHomeBanking.payments.components.date

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.DatePickerFragment
import com.example.rodrigobrodrigues.accentureHomeBanking.util.DateHandler
import kotlinx.android.synthetic.main.component_date_view.*

class DateView : Fragment(), DateContract.View, DatePickerFragment.OnDatePickerListener {

    private var presenter: DateContract.Presenter? = null
    private var listener: OnDateUpdateListener? = null

    interface OnDateUpdateListener {
        fun onDatePicker(day: Int, month: Int, year: Int)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.component_date_view, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val sharedPreferences = context
                .getSharedPreferences("com.example.rodrigobrodrigues.accentureHomeBanking",
                        MODE_PRIVATE)
        this.presenter = DatePresenter(this, sharedPreferences)
        btn_date.setOnClickListener{
            val dateDialog = DatePickerFragment()
            val args = Bundle()
            args.putInt("day", presenter!!.day)
            args.putInt("month", presenter!!.month)
            args.putInt("year", presenter!!.year)
            dateDialog.arguments = args
            dateDialog.setTargetFragment(this, 0)
            dateDialog.show(fragmentManager, "DateDialog")
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity: Activity

        if (context is Activity) {
            activity = context
            listener = activity as OnDateUpdateListener
        }
    }

    override fun setDate(day: Int, month: Int, year: Int) {
        val connector = getString(DateHandler.getConnectorID(day))
        val monthString = getString(DateHandler.getMonthID(month))
        val yearConnect = getString(R.string.date_connect_2)
        btn_date.hint = "$day$connector$monthString$yearConnect$year"
    }

    override fun onDatePick(day: Int, month: Int, year: Int) {
        presenter!!.updateDate(day, month, year)
        listener!!.onDatePicker(day, month, year)
    }
}
