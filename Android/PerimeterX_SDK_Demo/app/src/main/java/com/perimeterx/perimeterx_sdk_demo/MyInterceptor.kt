package com.perimeterx.android_sdk_demo

import com.perimeterx.mobile_sdk.PerimeterX
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MyInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()

        // When requestsInterceptedAutomaticallyEnabled is set to false in the policy => fetch HTTP headers from PerimeterX and add them to your request
        val headers = PerimeterX.INSTANCE.headersForURLRequest(null)
        for ((key, value) in headers) {
            newRequest.addHeader(key, value)
        }

        val response = chain.proceed(newRequest.build())
        if (!response.isSuccessful) {
            // The code below is an example to how you can check that the request was blocked. This is not required
            val responseBody = response.body?.string()
            if (responseBody != null) {
                // When requestsInterceptedAutomaticallyEnabled is set to true in the policy => check that the error is "Request blocked by PerimeterX"
                val isRequestBlockedError = PerimeterX.INSTANCE.isRequestBlockedError(responseBody)
                if (isRequestBlockedError) {
                    println("request was blocked by PX")
                }

                // When requestsInterceptedAutomaticallyEnabled is set to false in the policy => pass the data and response to PerimeterX to handle it
                val isHandledByPX = PerimeterX.INSTANCE.handleResponse(null, responseBody, response.code)
                if (isHandledByPX) {
                    println("block response was handled by PX")
                    // Replace the original response with a specific blocked error
                    return response.newBuilder().body(PerimeterX.INSTANCE.blockedErrorBody().toResponseBody()).build()
                }

                // Put back the response's body (can be read only once and we just did)
                return response.newBuilder().body(responseBody.toResponseBody()).build()
            }
        }
        return response
    }
}
