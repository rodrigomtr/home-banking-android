package com.example.rodrigobrodrigues.accentureHomeBanking.payments

import android.content.SharedPreferences

import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.models.Account

import java.util.Calendar
import java.util.Date

open class BasePaymentPresenter(private val view: BasePaymentContract.View,
                                private val sharedPreferences: SharedPreferences)
    : BasePaymentContract.Presenter {

    override val date: String
        get() {
            var day = day
            var month = month
            var year = year
            if (day == 0) {
                val calendar = Calendar.getInstance()
                calendar.time = Date()
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
            }
            return view.getDateString(day, month, year)
        }

    val account: Account
        get() = App.instance!!
                .user!!
                .client
                .accounts[sharedPreferences.getInt("account", 0)]

    val year: Int
        get() = sharedPreferences.getInt("year", 0)

    val month: Int
        get() = sharedPreferences.getInt("month", 0)

    protected val day: Int
        get() = sharedPreferences.getInt("day", 0)
}
