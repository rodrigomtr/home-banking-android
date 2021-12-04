package com.example.rodrigobrodrigues.accentureHomeBanking.payments

interface BasePaymentContract {
    interface View {
        fun getDateString(day: Int, month: Int, year: Int): String
    }

    interface Presenter {

        val date: String

    }
}
