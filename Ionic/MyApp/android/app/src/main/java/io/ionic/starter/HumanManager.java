package io.ionic.starter;

import android.app.Application;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.perimeterx.mobile_sdk.HumanSecurity;
import com.perimeterx.mobile_sdk.main.policy.HSPolicy;
import com.perimeterx.mobile_sdk.main.policy.HSAutomaticInterceptorType;
import com.perimeterx.mobile_sdk.HumanChallengeResult;
import java.util.HashMap;

@CapacitorPlugin(name = "Human")
public class HumanManager extends Plugin {

    String strSolved = "solved";
    String strCancelled = "cancelled";
    String strFalse = "false";
    String strValue = "value";

    static void startHumanSDK(Application application) {
        HSPolicy policy = new HSPolicy();
        policy.getAutomaticInterceptorPolicy().setInterceptorType(HSAutomaticInterceptorType.NONE);
        policy.getDoctorAppPolicy().setEnabled(true);
        try {
            HumanSecurity.INSTANCE.start(application, "PXj9y4Q8Em", null, policy);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @PluginMethod()
    public void getHttpHeaders(PluginCall call) {
        HashMap headers = HumanSecurity.INSTANCE.headersForURLRequest(null);
        JSObject ret = new JSObject();
        for (Object key : headers.keySet()) {
            ret.put((String)key, headers.get(key));
        }
        call.resolve(ret);
    }

    @PluginMethod()
    public void handleResponse(PluginCall call) {
        String response = call.getString(strValue);
        JSObject ret = new JSObject();
        boolean handled = HumanSecurity.INSTANCE.handleResponse(response, null, result -> {
            ret.put(strValue, result == HumanChallengeResult.SOLVED ? strSolved : strCancelled);
            call.resolve(ret);
            return null;
        });
        if (!handled) {
            ret.put(strValue, strFalse);
            call.resolve(ret);
        }
    }
}
