package com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks

import android.util.Log
import com.example.rodrigobrodrigues.accentureHomeBanking.App

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
 * @author Rodrigo Rodrigues on 20/03/2018.
 */

class PaymentTask {

    suspend fun pay(request: String): Int{
        val url = Strings.url + "/pay/execute"
        val tag = "SPRING_SERVER"

        val restTemplate = RestTemplate()
        restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())

        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        val result = async(CommonPool) {
            try {
                val entity = HttpEntity(request, headers)
                val response = restTemplate.exchange(url, HttpMethod.POST, entity, Int::class.java)
                response.body
            } catch (e: Exception) {
                Log.d(tag, e.localizedMessage)
                -1
            }
        }.await()
        UserTask().updateUser(App.instance!!.user!!.id)
        return result
    }
}
