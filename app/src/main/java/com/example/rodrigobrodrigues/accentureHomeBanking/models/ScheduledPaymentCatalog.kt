package com.example.rodrigobrodrigues.accentureHomeBanking.models

import java.io.Serializable

/**
 * @author Rodrigo Rodrigues on 15/03/2018.
 */

data class ScheduledPaymentCatalog(var catalog: MutableList<ScheduledPayment> = ArrayList())
    : Serializable
