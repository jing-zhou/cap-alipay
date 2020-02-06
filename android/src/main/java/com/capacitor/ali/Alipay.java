package com.capacitor.ali;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.alipay.sdk.app.PayTask;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

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
        final String bstr = call.getString(bill);

        CompletableFuture<Map<String, String>> future = CompletableFuture.supplyAsync(new Supplier<Map<String, String>>() {
            @Override
            public Map<String, String> get() {
                return (new PayTask(getActivity()).payV2(bstr, true));
            }
        });

        try {
            Map<String, String> res = future.get();
            String state = res.get(status);
            if (success.equals(state)) {
                JSObject ret = new JSObject();
                for (Map.Entry<String, String> e : res.entrySet()) {
                    ret.put(e.getKey(), e.getValue());
                }
                call.resolve(ret);
                return;
            }

            call.reject(state);
        } catch (ExecutionException | InterruptedException e) {
            call.reject(e.getMessage());

        }

    }

}
