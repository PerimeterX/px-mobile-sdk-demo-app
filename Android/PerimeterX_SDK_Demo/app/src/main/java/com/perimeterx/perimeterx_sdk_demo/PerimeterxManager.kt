package com.perimeterx.perimeterx_sdk_demo

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import com.perimeterx.mobile_sdk.PerimeterX
import com.perimeterx.mobile_sdk.PerimeterXDelegate
import com.perimeterx.mobile_sdk.main.PXPolicy

object PerimeterxManager: PerimeterXDelegate {

    // PerimeterXDelegate

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
        println("SDK version: ${PerimeterX.INSTANCE.sdkVersion()}")

        start(application, pxAppId)
        setPolicy()
        setCustomParameters()
    }

    private fun start(application: Application, appId: String) {
        // Start PerimeterX SDK with your AppID //
        PerimeterX.INSTANCE.start(application, appId, this, true) { success ->
            if (success) {
                PerimeterX.INSTANCE.vid(null)?.let { vid ->
                    println("vid: $vid")
                }
            }
            else {
                println("start was failed")

                Handler(Looper.getMainLooper()).postDelayed(1000) {
                    // Make sure to start PerimeterX SDK again when it fails (network issue, etc.) //
                    start(application, appId)
                }
            }
        }
    }

    private fun setPolicy() {
        val policy = PXPolicy()
        policy.requestsInterceptedAutomaticallyEnabled = true
        PerimeterX.INSTANCE.setPolicy(policy, null)
    }

    private fun setCustomParameters() {
        val customParameters = HashMap<String, String>()
        customParameters["custom_param1"] = "hello"
        customParameters["custom_param2"] = "world"
        PerimeterX.INSTANCE.setCustomParameters(customParameters, null)
    }
}
