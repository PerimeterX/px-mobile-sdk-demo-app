package com.perimeterx_sdk_demo;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.perimeterx.mobile_sdk.PerimeterX;
import com.perimeterx.mobile_sdk.PerimeterXChallengeResult;

import org.json.JSONObject;

import java.util.HashMap;

public class PerimeterXModule extends ReactContextBaseJavaModule {

    static PerimeterXModule shared = null;

    String pxNewHeaders = "PxNewHeaders";
    String pxChallengeResult = "PxChallengeResult";
    String pxSolved = "solved";
    String pxCancelled = "cancelled";
    String pxFalse = "false";

    // PX Module

    PerimeterXModule(ReactApplicationContext context) {
        super(context);
    }

    @NonNull
    @Override
    public String getName() {
        return "PerimeterXModule";
    }

    public void handleUpdatedHeaders(HashMap<String, String> headers) {
        if (!this.getReactApplicationContext().hasCatalystInstance()) {
            return;
        }
        JSONObject json = new JSONObject(headers);
        this.getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(pxNewHeaders, json.toString());
    }

    public void handleChallengeSolvedEvent() {
        if (!this.getReactApplicationContext().hasCatalystInstance()) {
            return;
        }
        this.getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(pxChallengeResult, pxSolved);
    }

    public void handleChallengeCancelledEvent() {
        if (!this.getReactApplicationContext().hasCatalystInstance()) {
            return;
        }
        this.getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(pxChallengeResult, pxCancelled);
    }

    @ReactMethod
    public void getHTTPHeaders(Promise promise) {
        JSONObject json = new JSONObject(PerimeterX.INSTANCE.headersForURLRequest(null));
        promise.resolve(json.toString());
    }

    @ReactMethod
    public void handleResponse(String response, Integer code, String url, Promise promise) {
        boolean handled = PerimeterX.INSTANCE.handleResponse(response, null, result -> {
            promise.resolve(result == PerimeterXChallengeResult.SOLVED ? pxSolved : pxCancelled);
            return null;
        });
        if (!handled) {
            promise.resolve(pxFalse);
        }
    }
}
