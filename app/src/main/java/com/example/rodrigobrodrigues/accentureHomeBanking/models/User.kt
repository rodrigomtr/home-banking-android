package com.example.rodrigobrodrigues.accentureHomeBanking.models

import java.io.Serializable

/**
 * @author Rodrigo Rodrigues on 11/01/2018.
 */

data class User(var id: Int = 0,
                var password: String = "",
                var client: Client = Client(),
                var active: Int = 0,
                var role: String = "",
                var matrix: Matrix? = null,
                var tries: Int = 0)
    : Serializable
