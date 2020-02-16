package com.capacitor.ali;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.alipay.sdk.app.PayTask;

import java.util.Map;

@NativePlugin()
public class Alipay extends Plugin {
    private static final String bill = "bill";
    private static final String status = "resultStatus";
    private static final String success = "9000";
    private static final String nobill = "Must provide the bill string";

    @PluginMethod()
    public void pay(PluginCall call) {
        if (!call.getData().has(bill)) {
            call.reject(nobill);
            return;
        }

        saveCall(call);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // Get the previously saved call
                PluginCall savedCall = getSavedCall();
                if (savedCall == null) {
                    return;
                }

                Map<String, String> res = new PayTask(getActivity()).payV2(savedCall.getString(bill), true);
                String state = res.get(status);
                if (success.equals(state)) {
                    JSObject ret = new JSObject();
                    for (Map.Entry<String, String> e : res.entrySet()) {
                        ret.put(e.getKey(), e.getValue());
                    }
                    savedCall.resolve(ret);
                    return;
                }

                savedCall.reject(state);
            }
        }).start();

    }

}
