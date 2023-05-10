package com.perimeterx.android_sdk_demo

import com.perimeterx.mobile_sdk.PerimeterX
import com.perimeterx.mobile_sdk.main.PXInterceptor
import io.ktor.client.call.body
import okhttp3.OkHttpClient
import okhttp3.Request

object OkHttpClientExample {

    private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
//        .addInterceptor(MyInterceptor()) // An example of manual integration. Should be added when PXPolicy.urlRequestInterceptionType is set to `PXPolicyUrlRequestInterceptionType/none`
        .addInterceptor(PXInterceptor()) // When PXPolicy.urlRequestInterceptionType is set to any value rather than `PXPolicyUrlRequestInterceptionType/none`. MUST BE THE LAST INTERCEPTOR IN THE CHAIN
        .build()

    fun sendLoginRequest(email: String, password: String) {
        try {
            val request: Request = Request.Builder().url(APIDataManager.loginUrl).build()
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        if (PerimeterX.isRequestBlockedError(responseBody)) {
                            println("request was blocked by PX")
                        }
                        if (PerimeterX.isChallengeSolvedError(responseBody)) {
                            println("request was blocked by PX and user solved the challenge")
                        }
                        if (PerimeterX.isChallengeCancelledError(responseBody)) {
                            println("request was blocked by PX and challenge was cancelled")
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
