package com.humansecurity.human_sdk_demo

import com.humansecurity.mobile_sdk.HumanSecurity
import com.humansecurity.mobile_sdk.main.HSInterceptor
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.statement.*

object KtorClientExample {

    private val ktorHttpClient: HttpClient = HttpClient(OkHttp) {
        install(HttpTimeout) {
            requestTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
        }
        engine {
//            addInterceptor(MyInterceptor()) // An example of manual integration. Should be added when PXPolicy.urlRequestInterceptionType is set to `PXPolicyUrlRequestInterceptionType/none`
            addInterceptor(HSInterceptor()) // When PXPolicy.urlRequestInterceptionType is set to any value rather than `PXPolicyUrlRequestInterceptionType/none`. MUST BE THE LAST INTERCEPTOR IN THE CHAIN
        }
    }

    // URL requests

    suspend fun sendLoginRequest(email: String, password: String, ) {
        try {
            val response: HttpResponse = ktorHttpClient.request(APIDataManager.loginUrl) {}
            println("request was finished")
            val responseBody = response.body<String>()
            if (responseBody.contains(HumanSecurity.blockedErrorBody())) {
                println("request was blocked by HUMAN")
            }
            if (responseBody.contains(HumanSecurity.challengeSolvedErrorBody())) {
                println("request was blocked by HUMAN and user solved the challenge")
            }
            if (responseBody.contains(HumanSecurity.challengeCancelledErrorBody())) {
                println("request was blocked by HUMAN and challenge was cancelled")
            }
        } catch (exception: Exception) {
            println("request was failed. exception: $exception")
        }
    }
}
