package com.example.rodrigobrodrigues.accentureHomeBanking.payments.components.account

import android.content.SharedPreferences

import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.util.AccountHandler
import com.example.rodrigobrodrigues.accentureHomeBanking.util.CurrencyHandler

class AccountPresenter (private val view: AccountContract.View, private val sharedPreferences: SharedPreferences)
    : AccountContract.Presenter {

    override fun setAccount(position: Int) {
        sharedPreferences.edit().putInt("account", position).apply()
        val account = App.instance!!.user!!.client.accounts[position]
        view.setAccountHint(AccountHandler.displayAccountHint(account.id, account.type))
        val balance = CurrencyHandler.displayCurrency(account.currentBalance)
        view.updateBalance(balance)
    }

    override fun updateView() {
        val currAccount = sharedPreferences.getInt("account", 0)
        try {
            setAccount(currAccount)
        } catch (e: IndexOutOfBoundsException) {
            setAccount(0)
        }
    }
}
