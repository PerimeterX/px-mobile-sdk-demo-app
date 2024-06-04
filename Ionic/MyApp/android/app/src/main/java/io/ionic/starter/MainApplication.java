package io.ionic.starter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.humansecurity.mobile_sdk.HumanDelegate;
import com.humansecurity.mobile_sdk.HumanSecurity;
import com.humansecurity.mobile_sdk.main.policy.HSAutomaticInterceptorType;
import com.humansecurity.mobile_sdk.main.policy.HSPolicy;
import com.humansecurity.mobile_sdk.main.policy.HSStorageMethod;

import java.util.HashMap;

public class MainApplication extends Application implements HumanDelegate {

    // Application

    @Override
    public void onCreate() {
        super.onCreate();

        HSPolicy policy = new HSPolicy();
        policy.setStorageMethod(HSStorageMethod.DATA_STORE);
        policy.getAutomaticInterceptorPolicy().setInterceptorType(HSAutomaticInterceptorType.NONE);
        policy.getDoctorAppPolicy().setEnabled(true);
        try {
            HumanSecurity.INSTANCE.start(this, "PXj9y4Q8Em", this, policy);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // PerimeterXDelegate

    @Override
    public void humanChallengeCancelledHandler(@NonNull String s) {

    }

    @Override
    public void humanChallengeSolvedHandler(@NonNull String s) {

    }

    @Override
    public void humanHeadersWereUpdated(@NonNull HashMap<String, String> hashMap, @NonNull String s) {

    }

    @Override
    public void humanRequestBlockedHandler(@Nullable String s, @NonNull String s1) {

    }

    @Override
    public void humanChallengeRenderedHandler(@NonNull String s) {

    }

    @Override
    public void humanChallengeRenderFailedHandler(@NonNull String s) {

    }
}
