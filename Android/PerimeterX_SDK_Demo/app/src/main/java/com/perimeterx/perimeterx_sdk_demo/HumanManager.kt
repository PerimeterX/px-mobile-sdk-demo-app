package com.perimeterx.android_sdk_demo

import android.app.Application
import com.perimeterx.mobile_sdk.HumanSecurity
import com.perimeterx.mobile_sdk.HumanDelegate
import com.perimeterx.mobile_sdk.main.policy.HSPolicy
import com.perimeterx.mobile_sdk.main.policy.HSAutomaticInterceptorType

object HumanManager: HumanDelegate {

    private const val humanAppID = "PXj9y4Q8Em"

    fun start(application: Application) {
        println("HUMAN Security SDK v${HumanSecurity.sdkVersion()}")

        // Create and configure the policy.
        val policy = HSPolicy()

        // Set the automatic interceptor's type.
        // Set it to `NONE` in order to disable it and then use `MyInterceptor`.
        policy.automaticInterceptorPolicy.interceptorType =
            HSAutomaticInterceptorType.INTERCEPT_AND_RETRY_REQUEST

        // Enable the Doctor App - a tool that helps you to verify the integration.
        policy.doctorAppPolicy.enabled = true

        // Start the SDK with your AppID.
        HumanSecurity.start(application, humanAppID, this, policy)

        setCustomParametersForBotDefender()
    }

    private fun setCustomParametersForBotDefender() {
        // Set custom parameters for Bot Defender (optional).
        val customParameters = HashMap<String, String>()
        customParameters["custom_param1"] = "hello"
        customParameters["custom_param2"] = "world"
        HumanSecurity.setCustomParameters(customParameters, null)
    }

    // HumanDelegate

    override fun humanRequestBlockedHandler(url: String?, appId: String) {
        println("Request was blocked. URL = $url")
    }

    override fun humanChallengeSolvedHandler(appId: String) {
        println("Challenge was solved")
    }

    override fun humanChallengeCancelledHandler(appId: String) {
        println("Challenge was cancelled")
    }

    override fun humanHeadersWereUpdated(headers: HashMap<String, String>, appId: String) {
        println("Headers were updated. Headers = $headers")
    }
}
