package com.example.rodrigobrodrigues.accentureHomeBanking.models

import java.io.Serializable

/**
 * Class that represents the table Account from Bank database
 * @author Rodrigo Rodrigues on 06/02/2018.
 */
data class Account(var id: Long = 0, var type: String = "Credit", var currentBalance: Double = 0.0) : Serializable
