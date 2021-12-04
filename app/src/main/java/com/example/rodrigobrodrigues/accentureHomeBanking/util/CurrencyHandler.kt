package com.example.rodrigobrodrigues.accentureHomeBanking.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * @author Rodrigo Rodrigues on 12/03/2018.
 */
object CurrencyHandler {

    fun displayCurrency(value: Double?): String {
        val eur = Currency.getInstance("EUR")
        val formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY)
        formatter.currency = eur
        return formatter.format(value)
    }
}
