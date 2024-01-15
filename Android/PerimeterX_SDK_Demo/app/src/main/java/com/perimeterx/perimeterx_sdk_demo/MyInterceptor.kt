package com.perimeterx.android_sdk_demo

import com.perimeterx.mobile_sdk.HumanSecurity
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/*
This is an example to how write a c custom interceptor that interacts with the SDK manually.
 */
class MyInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()

        // When `HSPolicy.automaticInterceptorPolicy.interceptorType` is set to `HSAutomaticInterceptorType/none` => get HTTP headers from the SDK and add them to your request.
        val headers = HumanSecurity.headersForURLRequest(null)
        for ((key, value) in headers!!) {
            newRequest.addHeader(key, value)
        }

        val response = chain.proceed(newRequest.build())
        if (!response.isSuccessful) {
            val responseBody = response.body?.string()
            if (responseBody != null) {
                // When `HSPolicy.automaticInterceptorPolicy.interceptorType` is set to any value rather than `HSAutomaticInterceptorType/none`  => check that the error is "The request was blocked by HUMAN".
                val isRequestBlockedError = HumanSecurity.isRequestBlockedError(responseBody)
                if (isRequestBlockedError) {
                    println("Request was blocked")
                }

                // When `HSPolicy.automaticInterceptorPolicy.interceptorType` is set to `HSAutomaticInterceptorType/none` => pass the data and response to the SDK.
                val isHandled = HumanSecurity.handleResponse(responseBody, null) { result ->
                    println("Challenge result = $result")
                }
                if (isHandled) {
                    println("Block response was handled by the SDK")
                    // Replace the original response with a specific blocked error.
                    return response.newBuilder().body(HumanSecurity.blockedErrorBody().toResponseBody()).build()
                }

                // Put back the response's body (can be read only once and we just did).
                return response.newBuilder().body(responseBody.toResponseBody()).build()
            }
        }
        return response
    }
}
