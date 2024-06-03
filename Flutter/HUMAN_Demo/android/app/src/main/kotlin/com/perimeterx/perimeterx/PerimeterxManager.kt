package com.perimeterx.perimeterx

import android.app.Application
import com.perimeterx.mobile_sdk.PerimeterX
import com.perimeterx.mobile_sdk.PerimeterXChallengeResult
import com.perimeterx.mobile_sdk.main.PXPolicy
import com.perimeterx.mobile_sdk.main.PXPolicyUrlRequestInterceptionType
import com.perimeterx.mobile_sdk.main.PXStorageMethod
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

class PerimeterxManager {

    companion object {

        fun start(application: Application) {
            try {
                val policy = PXPolicy()
                policy.storageMethod = PXStorageMethod.DATA_STORE
                policy.urlRequestInterceptionType = PXPolicyUrlRequestInterceptionType.NONE
                policy.doctorCheckEnabled = true
                PerimeterX.start(application, "PXj9y4Q8Em", null, policy)
            }
            catch (exception: Exception) {
                println("failed to start. exception: $exception")
            }
        }

        fun handleEvent(call: MethodCall, result: MethodChannel.Result) {
            if (call.method == "_getPxHeaders") {
                val json = JSONObject(PerimeterX.headersForURLRequest(null) as Map<*, *>?)
                result.success(json.toString())
            }
            else if (call.method == "_handlePxResponse") {
                val handled = PerimeterX.handleResponse(call.arguments!! as String, null) { challengeResult: PerimeterXChallengeResult ->
                    result.success(if (challengeResult == PerimeterXChallengeResult.SOLVED) "solved" else "cancelled")
                    null
                }
                if (!handled) {
                    result.success("false")
                }
            }
            else {
                result.notImplemented()
            }
        }
    }
}
