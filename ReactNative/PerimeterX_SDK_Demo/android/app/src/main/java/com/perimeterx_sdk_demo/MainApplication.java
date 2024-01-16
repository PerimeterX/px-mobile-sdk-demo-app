package com.perimeterx_sdk_demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.config.ReactFeatureFlags;
import com.facebook.soloader.SoLoader;
import com.perimeterx.mobile_sdk.HumanDelegate;
import com.perimeterx.mobile_sdk.HumanSecurity;
import com.perimeterx.mobile_sdk.main.policy.HSAutomaticInterceptorType;
import com.perimeterx.mobile_sdk.main.policy.HSPolicy;
import com.perimeterx_sdk_demo.newarchitecture.MainApplicationReactNativeHost;

public class MainApplication extends Application implements ReactApplication, HumanDelegate {

    private final ReactNativeHost mReactNativeHost =
            new ReactNativeHost(this) {
                @Override
                public boolean getUseDeveloperSupport() {
                    return BuildConfig.DEBUG;
                }

                @Override
                protected List<ReactPackage> getPackages() {
                    List<ReactPackage> packages = new PackageList(this).getPackages();
                    packages.add(new HumanPackage());
                    return packages;
                }

                @Override
                protected String getJSMainModuleName() {
                    return "index";
                }
            };

    private final ReactNativeHost mNewArchitectureNativeHost =
            new MainApplicationReactNativeHost(this);

    @Override
    public ReactNativeHost getReactNativeHost() {
        if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
            return mNewArchitectureNativeHost;
        } else {
            return mReactNativeHost;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ReactFeatureFlags.useTurboModules = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;
        SoLoader.init(this, false);
        initializeFlipper(this, getReactNativeHost().getReactInstanceManager());

        startHumanSDK();
    }

    private static void initializeFlipper(
            Context context, ReactInstanceManager reactInstanceManager) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> aClass = Class.forName("com.perimeterx_sdk_demo.ReactNativeFlipper");
                aClass
                        .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
                        .invoke(null, context, reactInstanceManager);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void startHumanSDK() {
        HSPolicy policy = new HSPolicy();
        policy.getAutomaticInterceptorPolicy().setInterceptorType(HSAutomaticInterceptorType.NONE);
        policy.getDoctorAppPolicy().setEnabled(true);
        try {
            HumanSecurity.INSTANCE.start(this, "PXj9y4Q8Em", this, policy);
        }
        catch (Exception exception) {
            Log.e("MainApplication","Exception: " + exception.getMessage());
        }
    }

    @Override
    public void humanRequestBlockedHandler(@Nullable String s, @NonNull String s1) {

    }

    @Override
    public void humanChallengeCancelledHandler(@NonNull String s) {
        HumanModule.shared.handleChallengeCancelledEvent();
    }

    @Override
    public void humanChallengeSolvedHandler(@NonNull String s) {
        HumanModule.shared.handleChallengeSolvedEvent();
    }

    @Override
    public void humanHeadersWereUpdated(@NonNull HashMap<String, String> hashMap, @NonNull String s) {
        if (HumanModule.shared != null) {
            HumanModule.shared.handleUpdatedHeaders(hashMap);
        }
    }
}
