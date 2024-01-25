package com.perimeterx.perimeterx

import android.app.Application
import com.perimeterx.mobile_sdk.HumanSecurity
import com.perimeterx.mobile_sdk.main.policy.HSPolicy
import com.perimeterx.mobile_sdk.main.policy.HSAutomaticInterceptorType
import com.perimeterx.mobile_sdk.HumanChallengeResult
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

class HumanManager {

    companion object {

        fun start(application: Application) {
            try {
                val policy = HSPolicy()
                policy.automaticInterceptorPolicy.interceptorType = HSAutomaticInterceptorType.NONE
                policy.doctorAppPolicy.enabled = true
                HumanSecurity.start(application, "PXj9y4Q8Em", null, policy)
            }
            catch (exception: Exception) {
                println("failed to start. exception: $exception")
            }
        }

        fun handleEvent(call: MethodCall, result: MethodChannel.Result) {
            if (call.method == "humanGetHeaders") {
                val json = JSONObject((HumanSecurity.headersForURLRequest(null) as Map<*, *>?))
                result.success(json.toString())
            }
            else if (call.method == "humanHandleResponse") {
                val handled = HumanSecurity.handleResponse(call.arguments!! as String, null) { challengeResult: HumanChallengeResult ->
                    result.success(if (challengeResult == HumanChallengeResult.SOLVED) "solved" else "cancelled")
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
