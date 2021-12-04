package com.example.rodrigobrodrigues.accentureHomeBanking.payments.components.date

import android.content.SharedPreferences
import java.util.*

class DatePresenter internal constructor(private val view: DateContract.View, private val sharedPreferences: SharedPreferences)
    : DateContract.Presenter {

    override val day: Int
        get() = sharedPreferences.getInt("day", 0)

    override val month: Int
        get() = sharedPreferences.getInt("month", 0)

    override val year: Int
        get() = sharedPreferences.getInt("year", 0)

    init {
        setCurrentDate()
    }

    private fun setCurrentDate() {
        val cal = Calendar.getInstance()
        cal.time = Date()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)

        sharedPreferences.edit().putInt("day", day).apply()
        sharedPreferences.edit().putInt("month", month).apply()
        sharedPreferences.edit().putInt("year", year).apply()

        view.setDate(day, month, year)
    }

    override fun updateDate(day: Int, month: Int, year: Int) {
        sharedPreferences.edit().putInt("day", day).apply()
        sharedPreferences.edit().putInt("month", month).apply()
        sharedPreferences.edit().putInt("year", year).apply()
        view.setDate(day, month, year)
    }
}
