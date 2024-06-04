package io.ionic.starter;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.humansecurity.mobile_sdk.HumanChallengeResult;
import com.humansecurity.mobile_sdk.HumanSecurity;
import java.util.HashMap;

@CapacitorPlugin(name = "HUMAN")
public class HumanManager extends Plugin {

    String humanSolved = "solved";
    String humanCancelled = "cancelled";
    String humanFalse = "false";

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
        String response = call.getString("value");
        JSObject ret = new JSObject();
        boolean handled = HumanSecurity.INSTANCE.handleResponse(response, null, result -> {
            ret.put("value", result == HumanChallengeResult.SOLVED ? humanSolved : humanCancelled);
            call.resolve(ret);
            return null;
        });
        if (!handled) {
            ret.put("value", humanFalse);
            call.resolve(ret);
        }
    }
}
