package com.perimeterx.android_sdk_demo

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import com.perimeterx.mobile_sdk.PerimeterX
import com.perimeterx.mobile_sdk.PerimeterXDelegate
import com.perimeterx.mobile_sdk.main.PXPolicy
import com.perimeterx.mobile_sdk.main.PXPolicyUrlRequestInterceptionType
import com.perimeterx.mobile_sdk.main.PXStorageMethod

object PerimeterxManager: PerimeterXDelegate {

    // PerimeterXDelegate

    override fun perimeterxRequestBlockedHandler(url: String?, appId: String) {
        println("PerimeterX Request Blocked")
    }

    override fun perimeterxChallengeSolvedHandler(appId: String) {
        println("PerimeterX Challenge Solved")
    }

    override fun perimeterxChallengeCancelledHandler(appId: String) {
        println("PerimeterX Challenge Cancelled")
    }

    override fun perimeterxHeadersWereUpdated(headers: HashMap<String, String>, appId: String) {
        println("PerimeterX Headers Were Updated")
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
        policy.storageMethod = PXStorageMethod.DATA_STORE
        policy.urlRequestInterceptionType = PXPolicyUrlRequestInterceptionType.INTERCEPT_AND_RETRY_REQUEST
        policy.doctorCheckEnabled = true

        // Start PerimeterX SDK with your AppID //
        PerimeterX.start(application, appId, this, policy)
    }

    private fun setCustomParameters() {
        val customParameters = HashMap<String, String>()
        customParameters["custom_param1"] = "hello"
        customParameters["custom_param2"] = "world"
        PerimeterX.setCustomParameters(customParameters, null)
    }
}
