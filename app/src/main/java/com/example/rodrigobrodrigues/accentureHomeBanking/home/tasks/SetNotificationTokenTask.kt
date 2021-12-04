package com.example.rodrigobrodrigues.accentureHomeBanking.home.tasks

import android.util.Log

import com.example.rodrigobrodrigues.accentureHomeBanking.util.Strings

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

import android.content.ContentValues.TAG
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay

/**
 * Class that sends the refreshed notification token to server
 * @author Rodrigo Rodrigues on 22/03/2018.
 */
class SetNotificationTokenTask {

    fun save(info: String) {
        val url = Strings.url + "/user/setToken"

        val restTemplate = RestTemplate()
        restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())

        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)

        serverPost(info, headers, url, restTemplate)

    }

    private fun serverPost(info: String, headers: HttpHeaders, url: String, restTemplate: RestTemplate) {
        async(CommonPool) {
            try {
                val entity = HttpEntity(info, headers)
                restTemplate.exchange(url, HttpMethod.POST, entity, HttpStatus::class.java)
            } catch (e: Exception) {
                Log.d(TAG, e.localizedMessage)
                delay(10000)
                serverPost(info, headers, url, restTemplate)
            }
        }
    }
}