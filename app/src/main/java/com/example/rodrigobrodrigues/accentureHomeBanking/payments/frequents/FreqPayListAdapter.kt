package com.example.rodrigobrodrigues.accentureHomeBanking.payments.frequents

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.models.PaymentSender
import com.example.rodrigobrodrigues.accentureHomeBanking.util.CurrencyHandler

/**
 * @author Rodrigo Rodrigues on 06/03/2018
 */

internal class FreqPayListAdapter(private val mContext: Context, private val paymentList: List<PaymentSender>)
    : ArrayAdapter<PaymentSender>(mContext, 0, paymentList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItem = convertView
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.freq_payments_adapter, parent, false)

        val currentPayment = paymentList[position]

        val entity = listItem!!.findViewById<TextView>(R.id.tv_entity)
        entity.text = currentPayment.entity

        val ref = listItem.findViewById<TextView>(R.id.tv_ref)
        ref.text = currentPayment.ref

        val amount = listItem.findViewById<TextView>(R.id.tv_amount)
        amount.text = CurrencyHandler.displayCurrency(currentPayment.amount)

        return listItem
    }
}
