package com.humansecurity.human_sdk_demo

import com.humansecurity.mobile_sdk.HumanSecurity
import com.humansecurity.mobile_sdk.main.HSInterceptor
import com.humansecurity.mobile_sdk.main.HSTimeoutInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object OkHttpClientExample {

    private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(0, TimeUnit.SECONDS)
//        .addInterceptor(MyInterceptor()) // An example of manual integration. Should be added when HSPolicy.automaticInterceptorPolicy.interceptorType is set to `HSAutomaticInterceptorType/none`
        .addInterceptor(HSTimeoutInterceptor())
        .addInterceptor(HSInterceptor()) // When HSPolicy.automaticInterceptorPolicy.interceptorType is set to any value rather than `HSAutomaticInterceptorType/none`. MUST BE THE LAST INTERCEPTOR IN THE CHAIN
        .build()

    fun sendLoginRequest(email: String, password: String) {
        try {
            val request: Request = Request.Builder().url(APIDataManager.loginUrl).build()
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        if (HumanSecurity.isRequestBlockedError(responseBody)) {
                            println("request was blocked by HUMAN")
                        }
                        if (HumanSecurity.isChallengeSolvedError(responseBody)) {
                            println("request was blocked by HUMAN and user solved the challenge")
                        }
                        if (HumanSecurity.isChallengeCancelledError(responseBody)) {
                            println("request was blocked by HUMAN and challenge was cancelled")
                        }
                    }
                }
                else {
                    println("request was finished")
                }
            }
        } catch (exception: Exception) {
            println("request was failed. exception: $exception")
        }
    }
}
