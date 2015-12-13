package com.example.lenovo.osc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.osc.Stocks.Stock;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by amidalilah on 08-Dec-15.
 */
public class ShoppingCartActivity extends AppCompatActivity {
    private static final String TAG = "ShoppingCartActivity";
    private static final String TAG1 = "PayPal Payment";
    private  static final  int REQUEST_CODE_PAYMENT = 1 ;
    final CartItemAdapter cartItemAdapter = new CartItemAdapter(this);;

    protected TextView tvTotalPrice;
    protected ListView lvCartItems;

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("AeED8SfpVA7hkFonFRxm07S1e5c0g2RngxPGl0OvNTDxj-zZ_v-HU3YGpttynOpss-N1A94D3i9GLgsn");

    final Cart cart = CartHelper.getCart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        lvCartItems = (ListView) findViewById(R.id.lvCartItems);
        LayoutInflater layoutInflater = getLayoutInflater();

        //start paypal
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice2);
        tvTotalPrice.setText(String.valueOf(cart.getTotalPrice()));
        lvCartItems.addHeaderView(layoutInflater.inflate(R.layout.cart_header, lvCartItems, false));
        cartItemAdapter.updateCartItems(getCartItems(cart));
        lvCartItems.setAdapter(cartItemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_cart, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    private List<CartItem> getCartItems(Cart cart) {
        List<CartItem> cartItems = new ArrayList<CartItem>();
        Log.d(TAG, "Current shopping cart: " + cart);

        Map<Saleable, Integer> itemMap = (Map<Saleable, Integer>) cart.getItemWithQuantity();

        for (Map.Entry<Saleable, Integer> entry : itemMap.entrySet()) {
            CartItem cartItem = new CartItem();
            cartItem.setStock((Stock) entry.getKey());
            cartItem.setQuantity(entry.getValue());
            cartItems.add(cartItem);
        }

        Log.d(TAG, "Cart item list: " + cartItems);
        return cartItems;
    }

    /* Create the PayPalPayment object and launch the PaymentActivity intent, for example, when a button is pressed */
    public void purchase(View v) {
        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.


        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(ShoppingCartActivity.this,
                PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);

        /* send details to server */
        Runnable run = new Runnable() {
            @Override
            public void run() {
                getThingToBuy(cart.getTotalPrice().toString());
            }
        };
        Thread thr = new Thread(run);
        thr.start();

        Toast.makeText(getApplication(), cart.getTotalPrice().toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    //this need to change to what item that list in list view
    private PayPalPayment getThingToBuy(String paymentIntent) {
//        //--- include an item list, payment amount details
//        List<PayPalItem> items = new ArrayList<PayPalItem>();
//        int i = 0;
//        while (getCartItems(cart).get(i) != null) {
//            items.add(new PayPalItem(getCartItems(cart).get(i).getStock().getName(),
//                    getCartItems(cart).get(i).getQuantity(),
//                    new BigDecimal(getCartItems(cart).get(i).getStock().getPrice()),"testing","testing"));
//
//            i++;
//        }
        BigDecimal subtotal = new BigDecimal(cart.getTotalPrice().toString());
//        BigDecimal shipping = new BigDecimal("7.21");
//        BigDecimal tax = new BigDecimal("4.67");
//        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
//        BigDecimal amount = subtotal.add(shipping).add(tax);
//        PayPalPayment payment = new PayPalPayment(amount, "MYR", "Total payment >>", paymentIntent);
        PayPalPayment payment= new PayPalPayment(subtotal, "MYR", "Total payment :", paymentIntent);
//        payment.items(items).paymentDetails(paymentDetails);

        //--- set other optional fields like invoice_number, custom field, and soft_descriptor
        payment.custom("This is text that will be associated with the payment that the app can use.");

        return payment;
    }

    /*
     * Add app-provided shipping address to payment
     */
    private void addAppProvidedShippingAddress(PayPalPayment paypalPayment) {
        ShippingAddress shippingAddress =
                new ShippingAddress().recipientName("Mom Parker").line1("52 North Main St.")
                        .city("Austin").state("TX").postalCode("78729").countryCode("US");
        paypalPayment.providedShippingAddress(shippingAddress);
    }

    /*
     * Enable retrieval of shipping addresses from buyer's PayPal account
     */
    private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
        paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i(TAG1, confirm.toJSONObject().toString(4));
                    Toast.makeText(getApplication(), "hi1", Toast.LENGTH_LONG).show();
                    // TODO: send 'confirm' to your server for verification.
                    getThingToBuy(cart.getTotalPrice().toString());

                } catch (JSONException e) {
                    Log.e(TAG1, "an extremely unlikely failure occurred: ", e);
                    Toast.makeText(getApplication(), "hi2", Toast.LENGTH_LONG).show();
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getApplication(), "hi3", Toast.LENGTH_LONG).show();
            Log.i(TAG1, "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(getApplication(), "hi4", Toast.LENGTH_LONG).show();
            Log.i(TAG1, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    public void clear(View v){
        Log.d(TAG, "Clearing the shopping cart");
        cart.clear();
        cartItemAdapter.updateCartItems(getCartItems(cart));
        cartItemAdapter.notifyDataSetChanged();
        tvTotalPrice.setText(String.valueOf(cart.getTotalPrice()));
    }

    public void shop(View v){
        finish();
    }
}