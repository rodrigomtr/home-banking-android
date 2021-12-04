package com.example.rodrigobrodrigues.accentureHomeBanking.payments.components.account

interface AccountContract {
    interface Presenter {

        fun setAccount(position: Int)

        fun updateView()
    }

    interface View {

        fun setAccountHint(s: String)

        fun updateBalance(s: String)
    }
}
