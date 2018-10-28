package com.awoshe.slydepaypayment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.awoshe.paywithslydepay.core.PayWithSlydepay;
import com.awoshe.paywithslydepay.core.SlydepayPayment;

/**
 * Created by Vijay Rathod
 */
public class SlydepayActivity extends Activity {
    private static final String TAG = SlydepayActivity.class.getSimpleName();
    public static String ITEM_NAME = "name";
    public static String AMOUNT = "amount";
    public static String DESCRIPTION = "description";
    public static String CUSTOMER_EMAIL = "customerEmail";
    public static String CUSTOMER_NAME = "customerName";
    public static String ORDER_CODE = "orderCode";
    public static String PHONE_NUMBER = "phoneNumber";
    public static String MERCHANT_KEY = "merchantKey";
    public static String MERCHANT_EMAIL = "merchantEmail";

    private int PAY_WITH_SLYDEPAY = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slydepay_payment);
        Intent intent = getIntent();
        new SlydepayPayment(getApplicationContext()).initCredentials(intent.getStringExtra(MERCHANT_EMAIL), intent.getStringExtra(MERCHANT_KEY));
        startPayment(intent);

    }

    /**
     * Start slydepay payment
     * @param intent slydepay activity intent
     */
    private void startPayment(Intent intent) {
        final Activity activity = this;
        String itemName = intent.getStringExtra(ITEM_NAME);
        double amount = intent.getExtras().getDouble(AMOUNT);
        String description = intent.getStringExtra(DESCRIPTION);
        String customerName = intent.getStringExtra(CUSTOMER_NAME);
        String customerEmail = intent.getStringExtra(CUSTOMER_EMAIL);
        String orderCode = intent.getStringExtra(ORDER_CODE);
        String phoneNumber = (intent.getStringExtra(PHONE_NUMBER));
        Log.e(TAG, "start Payment. " + itemName + " " + description + " " + amount);
        PayWithSlydepay.Pay(activity, itemName, amount, description, customerName, customerEmail, orderCode, phoneNumber, PAY_WITH_SLYDEPAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == PAY_WITH_SLYDEPAY){
            switch (resultCode){
                case RESULT_OK:
                    //Payment was successful
                    Toast.makeText(this, "Was successful", Toast.LENGTH_SHORT).show();
                    intent.putExtra("TYPE", 1);
                    setResult(Activity.RESULT_OK, intent);
                    break;
                case RESULT_CANCELED:
                    //Payment failed
                    intent.putExtra("TYPE", 2);
                    Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK, intent);
                    break;
                case RESULT_FIRST_USER:
                    //Payment was cancelled by user
                    intent.putExtra("TYPE", 3);
                    Toast.makeText(this, "User Canceled", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK, intent);
                    break;
            }
        }
        finish();
    }
}
