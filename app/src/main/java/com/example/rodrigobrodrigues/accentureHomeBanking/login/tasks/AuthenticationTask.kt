package com.example.rodrigobrodrigues.accentureHomeBanking.login.tasks

import android.util.Log

import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.models.User
import com.example.rodrigobrodrigues.accentureHomeBanking.util.Strings
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withTimeout

import org.springframework.http.HttpBasicAuthentication
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate

/**
 * @author Rodrigo Rodrigues on 23/03/2018.
 */
class AuthenticationTask {

    private var error: Int = 0
    private val tag = "SPRING"

    suspend fun login(username: String, password: String): Int {
        val url = Strings.url + "/android/auth"

        // Populate the HTTP Basic Authentication header with the username and password
        val authHeader = HttpBasicAuthentication(username, password)
        val requestHeaders = HttpHeaders()
        requestHeaders.setAuthorization(authHeader)
        requestHeaders.accept = listOf(MediaType.APPLICATION_JSON)

        // Create a new RestTemplate instance
        val restTemplate = RestTemplate()
        restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())
        try {
            val serverResponse = withTimeout(13000) {
                async(CommonPool) {
                    try {
                        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity<Any>(requestHeaders), User::class.java)
                        Log.d(tag, response.toString())
                        response.body
                    } catch (e: HttpClientErrorException) {
                        error = if (e.statusCode == HttpStatus.LOCKED)
                            R.string.error_blocked_account
                        else
                            R.string.error_auth
                        Log.e(tag, e.localizedMessage, e)
                        null
                    } catch (e: ResourceAccessException) {
                        error = R.string.error_acces
                        Log.e(tag, e.localizedMessage, e)
                        null
                    }
                }.await()
            }
            return if (serverResponse != null) {
                App.instance!!.user = serverResponse
                -1
            } else error
        } catch (e: CancellationException) {
            return R.string.error_acces
        }
    }
}