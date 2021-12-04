package com.example.rodrigobrodrigues.accentureHomeBanking.util

import com.example.rodrigobrodrigues.accentureHomeBanking.R

object DateHandler {

    fun getConnectorID(day: Int): Int {
        return when (day) {
            1 -> R.string.st
            2 -> R.string.nd
            3 -> R.string.rd
            else -> R.string.th
        }
    }
    fun getMonthID(value: Int): Int {
        return when (value) {
            0 -> R.string.january
            1 -> R.string.february
            2 -> R.string.march
            3 -> R.string.april
            4 -> R.string.may
            5 -> R.string.june
            6 -> R.string.july
            7 -> R.string.august
            8 -> R.string.september
            9 -> R.string.october
            10 -> R.string.november
            else -> R.string.december
        }
    }
}
