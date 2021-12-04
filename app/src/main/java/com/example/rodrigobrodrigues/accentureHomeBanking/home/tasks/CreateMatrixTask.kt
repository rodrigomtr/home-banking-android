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

/**
 * @author Rodrigo Rodrigues on 22/03/2018.
 */

class CreateMatrixTask(private val userID: Int) {

    suspend fun result(): Boolean{
        val url = Strings.url + "/user/createMatrix"

        val restTemplate = RestTemplate()
        restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())

        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)

        val serverResponse = async(CommonPool) {
            try {
                val entity = HttpEntity(userID.toString() + "", headers)
                val response = restTemplate.exchange(url, HttpMethod.POST, entity, HttpStatus::class.java)
                response.body
            } catch (e: Exception) {
                Log.d(TAG, e.localizedMessage)
                null
            }
        }.await()

        return serverResponse != null && serverResponse == HttpStatus.OK
    }
}

