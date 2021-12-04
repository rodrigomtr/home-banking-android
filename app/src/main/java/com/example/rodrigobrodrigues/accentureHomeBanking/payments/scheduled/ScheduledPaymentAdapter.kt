package com.example.rodrigobrodrigues.accentureHomeBanking.payments.scheduled

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView

import com.example.rodrigobrodrigues.accentureHomeBanking.util.CurrencyHandler
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.models.ScheduledPayment

/**
 * @author Rodrigo Rodrigues on 15/03/2018.
 */

internal class ScheduledPaymentAdapter(private val mContext: Context, private val paymentList: List<ScheduledPayment>)
    : ArrayAdapter<ScheduledPayment>(mContext, 0, paymentList) {

    private var isSelected: Boolean = false

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItem = convertView
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.sched_payments_adapter, parent, false)

        val currentPayment = paymentList[position]

        val description = listItem!!.findViewById<TextView>(R.id.tv_description)
        description.text = currentPayment.description

        val amount = listItem.findViewById<TextView>(R.id.tv_amount)
        amount.text = CurrencyHandler.displayCurrency(currentPayment.amount)

        val date = listItem.findViewById<TextView>(R.id.tv_date)
        date.text = currentPayment.date.toString()

        val checkBox = listItem.findViewById<CheckBox>(R.id.checkBox)

        if (isSelected) { checkBox.visibility = View.VISIBLE } else { checkBox.visibility = View.GONE }

        return listItem
    }

    fun setSelected(selected: Boolean) {
        isSelected = selected
    }
}
