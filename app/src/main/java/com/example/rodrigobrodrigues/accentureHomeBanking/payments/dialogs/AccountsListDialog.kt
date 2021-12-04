package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.TextSwitcher

import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.components.account.AccountsAdapter
import com.example.rodrigobrodrigues.accentureHomeBanking.util.CurrencyHandler


import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow

import android.content.Context.MODE_PRIVATE

/**
 * Dialog used to select an account
 *
 * @author Rodrigo Rodrigues on 06/02/2018.
 * @see [Dialog Fragment documentation](https://developer.android.com/reference/android/app/DialogFragment.html)
 */
class AccountsListDialog : DialogFragment() {
    private var listener: AccountsListDialog.OnSetAccountListener? = null

    /**
     * Interface definition for a callback to be invoked when an item of account list is selected.
     */
    interface OnSetAccountListener {
        /**
         * Callback method to be invoked when an item of account list is selected.
         * @param position The position of the account in account list
         */
        fun onSetAccount(position: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            listener = targetFragment as OnSetAccountListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Calling Fragment must implement OnAccountSetListener")
        }

    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.accounts_card_view, null)
        val title = view.findViewById<TextSwitcher>(R.id.ballance)
        title.setFactory {
            val inflater = LayoutInflater.from(context)
            inflater.inflate(R.layout.accounts_title, null)
        }

        val `in` = AnimationUtils.loadAnimation(activity, R.anim.slide_in_top)
        val out = AnimationUtils.loadAnimation(activity, R.anim.slide_out_bottom)

        title.inAnimation = `in`
        title.outAnimation = out

        val coverFlow = view.findViewById<FeatureCoverFlow>(R.id.cover_flow)
        val accountsAdapter = AccountsAdapter(App.instance?.user!!.client.accounts, context)
        coverFlow.adapter = accountsAdapter

        val sharedPreferences = activity
                .getSharedPreferences("com.example.rodrigobrodrigues.accentureHomeBanking",
                        MODE_PRIVATE)
        val currentAccount = sharedPreferences.getInt("account", 0)
        coverFlow.setOnScrollPositionListener(object : FeatureCoverFlow.OnScrollPositionListener {
            override fun onScrolledToPosition(position: Int) {
                title.setText(CurrencyHandler.displayCurrency(accountsAdapter.getItem(position).currentBalance))
            }

            override fun onScrolling() {

            }
        })
        if (currentAccount == 0) {
            coverFlow.scrollToPosition(accountsAdapter.count)
        } else {
            coverFlow.scrollToPosition(currentAccount)
        }
        return AlertDialog.Builder(activity)
                .setView(view)
                .setTitle(R.string.account_choose)
                .setPositiveButton(R.string.ok) { _, _ -> listener!!.onSetAccount(coverFlow.scrollPosition) }
                .setNegativeButton(R.string.cancel) { _, _ -> dismiss() }
                .create()
    }
}
