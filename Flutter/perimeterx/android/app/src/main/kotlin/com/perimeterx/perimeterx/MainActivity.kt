package com.perimeterx.perimeterx

import androidx.annotation.NonNull
import com.perimeterx.mobile_sdk.PerimeterX
import com.perimeterx.mobile_sdk.PerimeterXChallengeResult
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

class MainActivity: FlutterActivity() {

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "com.perimeterx.demo/perimeterx").setMethodCallHandler {
                call, result ->
            PerimeterxManager.handleEvent(call, result)
        }
    }
}
