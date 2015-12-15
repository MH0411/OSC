package com.example.lenovo.osc.StockistFunction;

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

import com.example.lenovo.osc.Cart.Cart;
import com.example.lenovo.osc.Cart.CartHelper;
import com.example.lenovo.osc.Cart.CartItem;
import com.example.lenovo.osc.Cart.CartItemAdapter;
import com.example.lenovo.osc.Cart.Saleable;
import com.example.lenovo.osc.Main.LoginActivity;
import com.example.lenovo.osc.R;
import com.example.lenovo.osc.Stocks.Stock;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private List<CartItem> cartItems;
    private ParseRelation<ParseObject> items ;
    private ArrayList soldQuantity = new ArrayList();
    private DecimalFormat df = new DecimalFormat("#.00");

    protected TextView tvTotalPrice;
    protected ListView lvCartItems;

    /**
     * Configure the paypal
     */
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
        tvTotalPrice.setText(df.format(cart.getTotalPrice()));
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
        cartItems = new ArrayList<CartItem>();
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

    /**
     * Create the PayPalPayment object and launch the PaymentActivity intent, for example,
     * when a button is pressed
     **/
    public void purchase(View v) {
        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.


        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(ShoppingCartActivity.this, PaymentActivity.class);
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
    }


    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    //this need to change to what item that list in list view
    private PayPalPayment getThingToBuy(String paymentIntent) {
        BigDecimal subtotal = new BigDecimal(df.format(cart.getTotalPrice()));
        PayPalPayment payment= new PayPalPayment(subtotal, "MYR", "Total payment :", paymentIntent);

        //--- set other optional fields like invoice_number, custom field, and soft_descriptor
        payment.custom("This is text that will be associated with the payment that the app can use.");

        return payment;
    }

//    /*
//     * Add app-provided shipping address to payment
//     */
//    private void addAppProvidedShippingAddress(PayPalPayment paypalPayment) {
//        ShippingAddress shippingAddress =
//                new ShippingAddress().recipientName("Mom Parker").line1("52 North Main St.")
//                        .city("Austin").state("TX").postalCode("78729").countryCode("US");
//        paypalPayment.providedShippingAddress(shippingAddress);
//    }
//
//    /*
//     * Enable retrieval of shipping addresses from buyer's PayPal account
//     */
//    private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
//        paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
//    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i(TAG1, confirm.toJSONObject().toString(4));
                    // TODO: send 'confirm' to your server for verification.

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Stockist");
                    query.getInBackground(LoginActivity.currentUser.getObjectID(),
                            new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject stockist, ParseException e) {
                            if (e == null) {

                                //Create a sale
                                Date date = new Date();
                                final ParseObject sale = new ParseObject("Sale");
                                sale.put("TotalPrice", cart.getTotalPrice());
                                sale.put("DateTime", date);
                                sale.put("StockistObjectID", stockist);
                                items = sale.getRelation("CentreStock");

                                //Loop to add item into items array to store in parse
                                for (int i = 0; i < getCartItems(cart).size(); i++) {

                                    ParseObject stock = ParseObject.createWithoutData("CentreStock",
                                            cartItems.get(i).getStock().getObjectID());
                                    soldQuantity.add(cartItems.get(i).getQuantity());
                                    items.add(stock);

                                    //Update centre stock
                                    ParseQuery query = ParseQuery.getQuery("CentreStock");
                                    final int finalI = i;
                                    query.getInBackground(cartItems.get(i).getStock().getObjectID()
                                            , new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(final ParseObject object, ParseException e) {
                                            object.put("Quantity", object.getNumber("Quantity").intValue()
                                                    - cartItems.get(finalI).getQuantity());
                                            object.saveInBackground();
                                        }
                                    });
                                }
                                sale.put("Quantity", soldQuantity);
                                sale.saveInBackground();

                                Toast.makeText(getApplication(), "Payment done", Toast.LENGTH_SHORT).show();
                                cart.clear();
                                finish();
                            }
                        }
                    });
                } catch (JSONException e) {
                    Log.e(TAG1, "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i(TAG1, "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i(TAG1, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    public void clear(View v){
        Log.d(TAG, "Clearing the shopping cart");
        cart.clear();
        cartItemAdapter.updateCartItems(getCartItems(cart));
        cartItemAdapter.notifyDataSetChanged();
        tvTotalPrice.setText(df.format(cart.getTotalPrice()));
    }

    public void shop(View v){
        finish();
    }
}