package com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks

import android.util.Log

import com.example.rodrigobrodrigues.accentureHomeBanking.util.Strings
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

/**
 * @author Rodrigo Rodrigues on 22/03/2018.
 */

class SendProvingEmailPdfTask {

    private val tag = "SPRING"

    suspend fun send(request: String): Boolean {
        val url = Strings.url + "/pay/get/proving/email"

        val restTemplate = RestTemplate()
        restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())

        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        val serverResponse = async(CommonPool) {
            try {
                val entity = HttpEntity(request, headers)
                val response = restTemplate.exchange(url, HttpMethod.POST, entity, HttpStatus::class.java)
                response.body
            } catch (e: Exception) {
                Log.d(tag, e.localizedMessage)
                null
            }
        }.await()
        return serverResponse == HttpStatus.OK
    }
}