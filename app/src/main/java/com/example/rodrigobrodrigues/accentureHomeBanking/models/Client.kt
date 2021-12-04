package com.example.rodrigobrodrigues.accentureHomeBanking.models

import java.io.Serializable
import java.util.ArrayList

/**
 * @author Rodrigo Rodrigues on 19/01/2018.
 */

data class Client(var id: Int = 0, var cc: String = "", var nif: String = "",
                  var firstName: String = "", var lastName: String = "",
                  var phone: String = "", var email: String = "",
                  var accounts: List<Account> = ArrayList()) : Serializable
