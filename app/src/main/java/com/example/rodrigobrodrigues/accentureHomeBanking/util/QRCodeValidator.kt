package com.example.rodrigobrodrigues.accentureHomeBanking.util

class QRCodeValidator(private val qrCode: String) {
    var entity: String? = null
    var ref: String? = null
    var amount: String? = null

    fun validate(): Boolean {
        val code = qrCode.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.entity = code[0]
        this.ref = code[1]
        this.amount = handleString(code[2])
        return (entity!!.matches("^[\\d]{5}$".toRegex())
                && (ref!!.matches("^[\\d]{9}$".toRegex()) || entity == "10095" && ref!!.matches("^[\\d]{15}$".toRegex()))
                && amount!!.matches("[\\d]+[.][\\d]+".toRegex()))
    }

    private fun handleString(s: String): String {
        return s.replace(",", ".")
    }
}
