package com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks

import android.util.Log

import com.example.rodrigobrodrigues.accentureHomeBanking.App

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate


import android.content.ContentValues.TAG
import com.example.rodrigobrodrigues.accentureHomeBanking.util.Strings
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

/**
 * @author Rodrigo Rodrigues on 22/03/2018.
 */

class CheckMatrixTask {

    suspend fun hasMatrix(): Boolean {
        val url = Strings.url + "/user/hasMatrix"

        val restTemplate = RestTemplate()
        restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())

        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)

        val result = async(CommonPool) {
            try {
                val entity = HttpEntity<String>(App.instance?.user?.id.toString(), headers)
                val response = restTemplate.exchange<HttpStatus>(url, HttpMethod.POST, entity, HttpStatus::class.java)
                response.body
            } catch (e: Exception) {
                Log.d(TAG, e.localizedMessage)
                null
            }
        }.await()
        return result == HttpStatus.OK
    }
}