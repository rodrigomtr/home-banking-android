package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.widget.TextView

import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.util.AccountHandler
import com.example.rodrigobrodrigues.accentureHomeBanking.util.CurrencyHandler

/**
 * @author Rodrigo Rodrigues on 04/04/2018.
 */

class ScheduledInfoDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity.layoutInflater
        @SuppressLint("InflateParams") val view = inflater.inflate(R.layout.scheduled_info_dialog, null)
        val accountTV = view.findViewById<TextView>(R.id.tv_account)
        val entityTV = view.findViewById<TextView>(R.id.tv_entity)
        val refTV = view.findViewById<TextView>(R.id.tv_ref)
        val amountTV = view.findViewById<TextView>(R.id.tv_amount)
        accountTV.text = AccountHandler.displayAccountId(arguments.getLong("account"))
        entityTV.text = arguments.getInt("entity").toString()
        refTV.text = arguments.getString("ref").replace("[\\w]{3}".toRegex(), "$0 ")
        amountTV.text = CurrencyHandler.displayCurrency(arguments.getDouble("amount"))
        return AlertDialog.Builder(activity)
                .setTitle(arguments.getString("description"))
                .setView(view)
                .create()
    }

    companion object {

        fun newInstance(account: Long, entity: Int, ref: String, amount: Double?, description: String): ScheduledInfoDialog {
            val args = Bundle()
            args.putLong("account", account)
            args.putInt("entity", entity)
            args.putString("ref", ref)
            args.putDouble("amount", amount!!)
            args.putString("description", description)
            val fragment = ScheduledInfoDialog()
            fragment.arguments = args
            return fragment
        }
    }
}
