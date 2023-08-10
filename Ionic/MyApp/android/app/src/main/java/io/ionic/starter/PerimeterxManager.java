package io.ionic.starter;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.perimeterx.mobile_sdk.PerimeterX;
import com.perimeterx.mobile_sdk.PerimeterXChallengeResult;
import java.util.HashMap;

@CapacitorPlugin(name = "PerimeterX")
public class PerimeterxManager extends Plugin {

    String pxSolved = "solved";
    String pxCancelled = "cancelled";
    String pxFalse = "false";

    @PluginMethod()
    public void getHttpHeaders(PluginCall call) {
        HashMap headers = PerimeterX.INSTANCE.headersForURLRequest(null);
        JSObject ret = new JSObject();
        for (Object key : headers.keySet()) {
            ret.put((String)key, headers.get(key));
        }
        call.resolve(ret);
    }

    @PluginMethod()
    public void handleResponse(PluginCall call) {
        String response = call.getString("value");
        JSObject ret = new JSObject();
        boolean handled = PerimeterX.INSTANCE.handleResponse(response, null, result -> {
            ret.put("value", result == PerimeterXChallengeResult.SOLVED ? pxSolved : pxCancelled);
            call.resolve(ret);
            return null;
        });
        if (!handled) {
            ret.put("value", pxFalse);
            call.resolve(ret);
        }
    }
}
