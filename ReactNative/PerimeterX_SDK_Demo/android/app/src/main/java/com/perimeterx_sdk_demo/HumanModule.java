package com.perimeterx_sdk_demo;

import androidx.annotation.NonNull;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.perimeterx.mobile_sdk.HumanSecurity;
import com.perimeterx.mobile_sdk.HumanChallengeResult;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Objects;

public class HumanModule extends ReactContextBaseJavaModule {

    static HumanModule shared = null;

    String strNewHeaders = "HumanNewHeaders";
    String strChallengeResult = "HumanChallengeResult";
    String strSolved = "solved";
    String strCancelled = "cancelled";
    String strFalse = "false";

    HumanModule(ReactApplicationContext context) {
        super(context);
    }

    @NonNull
    @Override
    public String getName() {
        return "HumanModule";
    }

    public void handleUpdatedHeaders(HashMap<String, String> headers) {
        if (!this.getReactApplicationContext().hasCatalystInstance()) {
            return;
        }
        JSONObject json = new JSONObject(headers);
        this.getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(strNewHeaders, json.toString());
    }

    public void handleChallengeSolvedEvent() {
        if (!this.getReactApplicationContext().hasCatalystInstance()) {
            return;
        }
        this.getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(strChallengeResult, strSolved);
    }

    public void handleChallengeCancelledEvent() {
        if (!this.getReactApplicationContext().hasCatalystInstance()) {
            return;
        }
        this.getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(strChallengeResult, strCancelled);
    }

    @ReactMethod
    public void getHTTPHeaders(Promise promise) {
        JSONObject json = new JSONObject(HumanSecurity.INSTANCE.headersForURLRequest(null));
        promise.resolve(json.toString());
    }

    @ReactMethod
    public void handleResponse(String response, Integer code, String url, Promise promise) {
        boolean handled = HumanSecurity.INSTANCE.handleResponse(response, null, result -> {
            promise.resolve(result == HumanChallengeResult.SOLVED ? strSolved : strCancelled);
            return null;
        });
        if (!handled) {
            promise.resolve(strFalse);
        }
    }
}
