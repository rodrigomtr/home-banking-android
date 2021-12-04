package com.example.rodrigobrodrigues.accentureHomeBanking.models

import java.io.Serializable
import java.sql.Date

/**
 * @author Rodrigo Rodrigues on 15/03/2018.
 */

data class ScheduledPayment(var id: Int = 0,
                            var description: String = "",
                            var amount: Double = 0.0,
                            var date: Date = Date(0),
                            var entity: Int = 0,
                            var ref: String = "",
                            var account: Long = 0)
    : Serializable