package com.perimeterx.android_sdk_demo

import com.perimeterx.mobile_sdk.PerimeterX
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/*
This is an example to how write a c custom interceptor that interacts with the SDK manually.
 */
class MyInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()

        // When PXPolicy.urlRequestInterceptionType is set to `PXPolicyUrlRequestInterceptionType/none` => get HTTP headers from PerimeterX and add them to your request //
        val headers = PerimeterX.headersForURLRequest(null)
        for ((key, value) in headers!!) {
            newRequest.addHeader(key, value)
        }

        val response = chain.proceed(newRequest.build())
        if (!response.isSuccessful) {
            // The code below is an example to how you can check that the request was blocked. This is not required
            val responseBody = response.body?.string()
            if (responseBody != null) {
                // When PXPolicy.urlRequestInterceptionType is set to any value rather than `PXPolicyUrlRequestInterceptionType/none`  => check that the error is "Request blocked by PerimeterX" //
                val isRequestBlockedError = PerimeterX.isRequestBlockedError(responseBody)
                if (isRequestBlockedError) {
                    println("request was blocked by PX")
                }

                // When PXPolicy.urlRequestInterceptionType is set to `PXPolicyUrlRequestInterceptionType/none` => pass the data and response to PerimeterX to handle it //
                val isHandledByPX = PerimeterX.handleResponse(responseBody, null) { result ->
                    println("challenge result = $result")
                }
                if (isHandledByPX) {
                    println("block response was handled by PX")
                    // Replace the original response with a specific blocked error
                    return response.newBuilder().body(PerimeterX.blockedErrorBody().toResponseBody()).build()
                }

                // Put back the response's body (can be read only once and we just did)
                return response.newBuilder().body(responseBody.toResponseBody()).build()
            }
        }
        return response
    }
}
