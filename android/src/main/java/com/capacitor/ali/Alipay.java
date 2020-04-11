package com.capacitor.ali;

import android.os.Handler;
import android.os.Message;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.alipay.sdk.app.PayTask;

import java.lang.ref.WeakReference;
import java.util.Map;

@SuppressWarnings("unused")
@NativePlugin()
public class Alipay extends Plugin {
    private static final int apay = 101;
    private static final String bill = "bill";
    private static final String status = "resultStatus";
    private static final String success = "9000";
    private static final String nobill = "Must provide the bill string";

    // not a perfect solution
    private PluginCall myCall;

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
                PluginCall ca = alipay.myCall;
                if (apay == msg.what) {
                    if (ca == null) {
                        return;
                    }
                    @SuppressWarnings("unchecked")
                    Map<String, String> res = (Map<String, String>) msg.obj;
                    String state = res.get(status);
                    if (success.equals(state)) {
                        JSObject ret = new JSObject();
                        for (Map.Entry<String, String> e : res.entrySet()) {
                            ret.put(e.getKey(), e.getValue());
                        }
                        ca.resolve(ret);
                        return;
                    }

                    ca.reject(state);

                }
            }
        }
    }

    private final MyHandler mHandler = new MyHandler(this);

    @SuppressWarnings("unused")
    @PluginMethod()
    public void pay(PluginCall call) {
        if (!call.getData().has(bill)) {
            call.reject(nobill);
            return;
        }

        final String bstr = call.getString(bill);
        myCall = call;

        new Thread(new Runnable() {

            @Override
            public void run() {

                Message msg = new Message();
                msg.what = apay;
                msg.obj = new PayTask(getActivity()).payV2(bstr, true);
                mHandler.sendMessage(msg);
            }
        }).start();

    }

}
