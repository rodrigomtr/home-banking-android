package com.example.rodrigobrodrigues.accentureHomeBanking.payments.components.date

interface DateContract {
    interface View {
        fun setDate(day: Int, month: Int, year: Int)
    }

    interface Presenter {

        val day: Int

        val month: Int

        val year: Int

        fun updateDate(day: Int, month: Int, year: Int)
    }
}
