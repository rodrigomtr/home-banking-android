package com.example.rodrigobrodrigues.accentureHomeBanking.models

import java.io.Serializable

/**
 * @author Rodrigo Rodrigues on 07/03/2018.
 */

data class PaymentSenderCatalog(var catalog: MutableList<PaymentSender> = ArrayList())
    : Serializable
