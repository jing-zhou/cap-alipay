package com.capacitor.ali;

import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@NativePlugin()
public class Alipay extends Plugin {
    private static final int apay = 101;
    private static final String bill = "bill";
    private static final String id = "callId";
    private static final String status = "resultStatus";
    private static final String success = "9000";
    private static final String nobill = "Must provide the bill string";

    private MyHandler _handler;
    private Map<String, PluginCall> _calls;

    @SuppressWarnings("unused")
    @PluginMethod()
    public void pay(final PluginCall call) {
        if (!call.getData().has(bill)) {
            call.reject(nobill);
            return;
        }

        final String bstr = call.getString(bill);

        new Thread(() -> {
            Message msg = new Message();
            msg.what = apay;
            String callId = call.getCallbackId();
            calls().put(callId, call);
            Map<String, String> res = new PayTask(getActivity()).payV2(bstr, true);
            res.put(id, callId);
            msg.obj = res;
            hanlder().sendMessage(msg);
        }).start();

    }

    private Map<String, PluginCall> calls() {
        if (_calls == null) {
            _calls = new HashMap<>();
        }
        return _calls;
    }

    private MyHandler hanlder() {
        if (_handler == null) {
            _handler = new MyHandler(this);
        }
        return _handler;
    }

    // https://stackoverflow.com/questions/11407943/this-handler-class-should-be-static-or-leaks-might-occur-incominghandler
    //static inner class doesn't hold an implicit reference to the outer class
    private static class MyHandler extends Handler {
        //Using a weak reference means you won't prevent garbage collection
        private final WeakReference<Alipay> alipayWeakReferencee;

        MyHandler(Alipay myClassInstance) {
            alipayWeakReferencee = new WeakReference<>(myClassInstance);
        }

        @Override
        public void handleMessage(Message msg) {
            Alipay alipay = alipayWeakReferencee.get();
            if (alipay != null) {
                if (apay == msg.what) {
                    Map<String, String> res = (Map<String, String>) msg.obj;
                    String callId = res.remove(id);
                    if (callId != null) {
                        PluginCall call = alipay.calls().remove(callId);
                        if (call != null) {
                            String state = res.get(status);
                            if (success.equals(state)) {
                                JSObject ret = new JSObject();
                                for (Map.Entry<String, String> e : res.entrySet()) {
                                    ret.put(e.getKey(), e.getValue());
                                }
                                call.resolve(ret);
                            }
                        }
                    }
                }
            }
        }
    }

}
