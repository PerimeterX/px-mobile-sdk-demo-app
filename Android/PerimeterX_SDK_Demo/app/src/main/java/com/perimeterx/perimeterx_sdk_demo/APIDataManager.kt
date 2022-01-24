package com.perimeterx.android_sdk_demo

import com.perimeterx.mobile_sdk.PerimeterX
import com.perimeterx.mobile_sdk.main.PXInterceptor

object APIDataManager {

    // properties

    private const val serverUrl = "https://sample-ios.pxchk.net/"
    const val loginUrl = serverUrl + "login"
    const val productsUrl = serverUrl + "products"

    suspend fun sendLoginRequest(email: String, password: String) {
        KtorClientExample.sendLoginRequest(email, password)
        OkHttpClientExample.sendLoginRequest(email, password)
    }
}
