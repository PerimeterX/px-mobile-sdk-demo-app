package com.humansecurity.human_sdk_demo

import android.app.Application
import com.humansecurity.mobile_sdk.HumanDelegate
import com.humansecurity.mobile_sdk.HumanSecurity
import com.humansecurity.mobile_sdk.main.policy.HSAutomaticInterceptorType
import com.humansecurity.mobile_sdk.main.policy.HSPolicy
import com.humansecurity.mobile_sdk.main.policy.HSStorageMethod

object HumanManager: HumanDelegate {

    // HumanDelegate

    override fun humanRequestBlockedHandler(url: String?, appId: String) {
        println("Request Blocked")
    }

    override fun humanChallengeSolvedHandler(appId: String) {
        println("Challenge Solved")
    }

    override fun humanChallengeCancelledHandler(appId: String) {
        println("Challenge Cancelled")
    }

    override fun humanChallengeRenderFailedHandler(appId: String) {
        println("Challenge Render Failed")
    }

    override fun humanChallengeRenderedHandler(appId: String) {
        println("Challenge Rendered")
    }

    override fun humanHeadersWereUpdated(headers: HashMap<String, String>, appId: String) {
        println("Headers Were Updated")
    }

    // properties

    const val appId = "PXj9y4Q8Em"

    // HUMAN

    fun start(application: Application) {
        println("SDK version: ${HumanSecurity.sdkVersion()}")

        start(application, appId)
        setCustomParameters()
    }

    private fun start(application: Application, appId: String) {
        // Create and configure the policy //
        val policy = HSPolicy()
        policy.storageMethod = HSStorageMethod.DATA_STORE
        policy.automaticInterceptorPolicy.interceptorType = HSAutomaticInterceptorType.INTERCEPT_AND_RETRY_REQUEST
        policy.doctorAppPolicy.enabled = true

        // Start HUMAN SDK with your AppID //
        HumanSecurity.start(application, appId, this, policy)
    }

    private fun setCustomParameters() {
        val customParameters = HashMap<String, String>()
        customParameters["custom_param1"] = "hello"
        customParameters["custom_param2"] = "world"
        HumanSecurity.setCustomParameters(customParameters, null)
    }
}
