package com.awoshe.slydepaypayment;

import android.app.Activity;
import android.content.Intent;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

/**
 * SlydepayPaymentPlugin create by Vijay Rathod
 */
public class SlydepayPaymentPlugin implements MethodCallHandler, PluginRegistry.ActivityResultListener {
    private Activity activity;
    private Result pendingResult;
    private int PAY_WITH_SLYDEPAY = 90;
    private static final String TAG = SlydepayPaymentPlugin.class.getSimpleName();
    private static final String ACTION_START_PAYMENT = "startSlydepayPayment";

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        String METHOD_CHANNEL_NAME = "slydepay_payment";
        final MethodChannel channel = new MethodChannel(registrar.messenger(), METHOD_CHANNEL_NAME);
        SlydepayPaymentPlugin instance = new SlydepayPaymentPlugin(registrar.activity(), channel);

        channel.setMethodCallHandler(instance);
        registrar.addActivityResultListener(instance);
    }

    private SlydepayPaymentPlugin(Activity activity, MethodChannel channel) {
        this.activity = activity;
        channel.setMethodCallHandler(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onMethodCall(MethodCall call, Result result) {
        pendingResult = result;
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals(ACTION_START_PAYMENT)) {
            Map<String, Object> params = (Map<String, Object>) call.arguments;
            Intent slydePayIntent = new Intent(activity, SlydepayActivity.class);

            slydePayIntent.putExtra(SlydepayActivity.AMOUNT, (Double) params.get("amount"));
            slydePayIntent.putExtra(SlydepayActivity.ITEM_NAME, (String) params.get("name"));
            slydePayIntent.putExtra(SlydepayActivity.DESCRIPTION, (String) params.get("description"));
            slydePayIntent.putExtra(SlydepayActivity.CUSTOMER_NAME, (String) params.get("customerName"));
            slydePayIntent.putExtra(SlydepayActivity.CUSTOMER_EMAIL, (String) params.get("customerEmail"));
            slydePayIntent.putExtra(SlydepayActivity.ORDER_CODE, (String) params.get("orderCode"));
            slydePayIntent.putExtra(SlydepayActivity.PHONE_NUMBER, (String) params.get("phoneNumber"));
            slydePayIntent.putExtra(SlydepayActivity.MERCHANT_EMAIL, (String) params.get("merchantEmail"));
            slydePayIntent.putExtra(SlydepayActivity.MERCHANT_KEY, (String) params.get("merchantKey"));

            activity.startActivityForResult(slydePayIntent, PAY_WITH_SLYDEPAY);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == PAY_WITH_SLYDEPAY) {
            List<String> data = new ArrayList<>();
            String response = intent.getStringExtra("TYPE");

            switch (resultCode) {
                case Activity.RESULT_OK:
                    //Payment was successful
                    Log.e(TAG, "Payment was successful ");
                    data.add(response);
                    data.add("SUCCESS");
                    pendingResult.success(data);
                    break;
                case Activity.RESULT_CANCELED:
                    //Payment failed
                    data.add(response);
                    data.add("FAILED");
                    Log.e(TAG, "Payment failed ");
                    pendingResult.success(data);
                    break;
                case Activity.RESULT_FIRST_USER:
                    //Payment was cancelled by user
                    data.add(response);
                    data.add("CANCELLED");
                    Log.e(TAG, "Payment was cancelled by user ");
                    pendingResult.success(data);
                    break;
            }
            pendingResult = null;
            return true;
        }
        return false;
    }
}
