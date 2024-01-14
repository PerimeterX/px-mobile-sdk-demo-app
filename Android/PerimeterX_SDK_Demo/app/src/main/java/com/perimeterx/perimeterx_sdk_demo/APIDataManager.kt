package com.perimeterx.android_sdk_demo

object APIDataManager {

    private const val serverUrl = "https://sample-ios.pxchk.net/"
    const val loginUrl = serverUrl + "login"
    private const val ktorExample = false

    suspend fun sendLoginRequest(email: String, password: String) {
        if (ktorExample) {
            KtorClientExample.sendLoginRequest(email, password)
        }
        else {
            OkHttpClientExample.sendLoginRequest(email, password)
        }
    }
}
