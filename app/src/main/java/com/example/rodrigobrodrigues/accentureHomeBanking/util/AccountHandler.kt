package com.example.rodrigobrodrigues.accentureHomeBanking.util

object AccountHandler {
    fun displayAccountId(id: Long): String {
        return id.toString().replace("[\\w]{3}".toRegex(), "$0 ")
    }

    fun displayAccountHint(id: Long, type: String): String {
        return (AccountHandler.displayAccountId(id)
                + " - " + type + " - EUR")
    }
}
