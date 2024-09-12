package io.ionic.starter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getcapacitor.BridgeActivity;
import com.perimeterx.mobile_sdk.PerimeterX;
import com.perimeterx.mobile_sdk.PerimeterXDelegate;
import com.perimeterx.mobile_sdk.main.PXPolicy;
import com.perimeterx.mobile_sdk.main.PXPolicyUrlRequestInterceptionType;
import com.perimeterx.mobile_sdk.main.PXStorageMethod;

import java.util.HashMap;

public class MainApplication extends Application implements PerimeterXDelegate {

    // Application

    @Override
    public void onCreate() {
        super.onCreate();

        PXPolicy policy = new PXPolicy();
        policy.setStorageMethod(PXStorageMethod.DATA_STORE);
        policy.setUrlRequestInterceptionType(PXPolicyUrlRequestInterceptionType.NONE);
        policy.setDoctorCheckEnabled(true);
        try {
            PerimeterX.INSTANCE.start(this, "PXj9y4Q8Em", this, policy);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // PerimeterXDelegate

    @Override
    public void perimeterxChallengeCancelledHandler(@NonNull String s) {

    }

    @Override
    public void perimeterxChallengeSolvedHandler(@NonNull String s) {

    }

    @Override
    public void perimeterxHeadersWereUpdated(@NonNull HashMap<String, String> hashMap, @NonNull String s) {

    }

    @Override
    public void perimeterxRequestBlockedHandler(@Nullable String s, @NonNull String s1) {

    }

    @Override
    public void perimeterxChallengeRenderFailedHandler(@NonNull String s) {

    }

    @Override
    public void perimeterxChallengeRenderedHandler(@NonNull String s) {

    }
}
