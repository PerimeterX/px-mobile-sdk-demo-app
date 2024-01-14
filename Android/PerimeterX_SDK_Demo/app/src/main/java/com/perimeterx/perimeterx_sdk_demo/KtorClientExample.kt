package com.perimeterx.android_sdk_demo

import com.perimeterx.mobile_sdk.HumanSecurity
import com.perimeterx.mobile_sdk.main.HSInterceptor
import com.perimeterx.mobile_sdk.main.HSTimeoutInterceptor
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.statement.*

object KtorClientExample {

    private const val requestTimeout = 10_000
    private val ktorHttpClient: HttpClient = HttpClient(OkHttp) {
        install(HttpTimeout) {
            requestTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS // Timeout must be set to be `INFINITE_TIMEOUT_MS` when `HSPolicy.automaticInterceptorPolicy.urlRequestInterceptionType` is set to any value rather than `HSAutomaticInterceptorType/none`.
        }
        engine {
//            addInterceptor(MyInterceptor()) // An example of basic implementation. Should be added when `HSPolicy.automaticInterceptorPolicy.urlRequestInterceptionType` is set to `HSAutomaticInterceptorType/none`.
            addInterceptor(HSTimeoutInterceptor(requestTimeout, requestTimeout, requestTimeout)) // When `HSPolicy.automaticInterceptorPolicy.urlRequestInterceptionType` is set to any value rather than `HSAutomaticInterceptorType/none`.
            addInterceptor(HSInterceptor()) // When `HSPolicy.automaticInterceptorPolicy.urlRequestInterceptionType` is set to any value rather than `HSAutomaticInterceptorType/none`. MUST BE THE LAST INTERCEPTOR IN THE CHAIN.
        }
    }

    suspend fun sendLoginRequest(email: String, password: String, ) {
        try {
            val response: HttpResponse = ktorHttpClient.request(APIDataManager.loginUrl) {}
            println("Request was finished")
            val responseBody = response.body<String>()
            if (responseBody.contains(HumanSecurity.blockedErrorBody())) {
                println("Request was blocked")
            }
            if (responseBody.contains(HumanSecurity.challengeSolvedErrorBody())) {
                println("Request was blocked and the user solved the challenge")
            }
            if (responseBody.contains(HumanSecurity.challengeCancelledErrorBody())) {
                println("Request was blocked and the challenge was cancelled")
            }
        } catch (exception: Exception) {
            println("Request was failed. Exception: $exception")
        }
    }
}
