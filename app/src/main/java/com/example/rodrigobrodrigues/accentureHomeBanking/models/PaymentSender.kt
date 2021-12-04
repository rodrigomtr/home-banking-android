package com.example.rodrigobrodrigues.accentureHomeBanking.models

import java.io.Serializable

/**
 * @author Rodrigo Rodrigues on 07/03/2018.
 */

data class PaymentSender(var entity: String = "",
                         var ref: String = "",
                         var amount: Double = 0.0) : Serializable
