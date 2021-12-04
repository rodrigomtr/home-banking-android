package com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks

import android.util.Log

import com.example.rodrigobrodrigues.accentureHomeBanking.models.PaymentSenderCatalog
import com.example.rodrigobrodrigues.accentureHomeBanking.util.Strings
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

/**
 * Rodrigo Rodrigues on 22/03/2018.
 */

class FrequentPaymentsTask {

    private val tag = "SPRING"

    suspend fun frequentPayments(accountID: String): PaymentSenderCatalog? {
        val url = Strings.url + "/pay/freqs"

        val restTemplate = RestTemplate()
        restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())

        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        return async(CommonPool) {
            try {
                val entity = HttpEntity(accountID, headers)
                val response = restTemplate.exchange(url, HttpMethod.POST, entity, PaymentSenderCatalog::class.java)
                response.body
            } catch (e: Exception) {
                Log.d(tag, e.localizedMessage)
                null
            }
        }.await()
    }
}