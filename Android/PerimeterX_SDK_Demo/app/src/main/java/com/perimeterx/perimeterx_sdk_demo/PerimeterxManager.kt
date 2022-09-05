package com.perimeterx.android_sdk_demo

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import com.perimeterx.mobile_sdk.PerimeterX
import com.perimeterx.mobile_sdk.PerimeterXDelegate
import com.perimeterx.mobile_sdk.main.PXPolicy
import com.perimeterx.mobile_sdk.main.PXPolicyUrlRequestInterceptionType

object PerimeterxManager: PerimeterXDelegate {

    // PerimeterXDelegate

    override fun perimeterxReady(appId: String) {

    }

    override fun perimeterxFailure(appId: String, error: String) {

    }

    override fun perimeterxRequestBlockedHandler(p0: String) {
        println("PerimeterX Request Blocked")
    }

    override fun perimeterxChallengeSolvedHandler(p0: String) {
        println("PerimeterX Challenge Solved")
    }

    override fun perimeterxChallengeCancelledHandler(p0: String) {
        println("PerimeterX Challenge Cancelled")
    }

    // properties

    const val pxAppId = "PXj9y4Q8Em"

    // PerimeterX

    fun start(application: Application) {
        println("SDK version: ${PerimeterX.sdkVersion()}")

        start(application, pxAppId)
        setCustomParameters()
    }

    private fun start(application: Application, appId: String) {
        // Create and configure the policy //
        val policy = PXPolicy()
        policy.urlRequestInterceptionType = PXPolicyUrlRequestInterceptionType.INTERCEPT_AND_RETRY_REQUEST
        policy.doctorCheckEnabled = true

        // Start PerimeterX SDK with your AppID //
        PerimeterX.start(application, appId, this, PXPolicy())
    }

    private fun setCustomParameters() {
        val customParameters = HashMap<String, String>()
        customParameters["custom_param1"] = "hello"
        customParameters["custom_param2"] = "world"
        PerimeterX.setCustomParameters(customParameters, null)
    }
}
