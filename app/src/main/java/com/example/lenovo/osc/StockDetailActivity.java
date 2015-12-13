package com.example.lenovo.osc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lenovo.osc.Stocks.Stock;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


public class StockDetailActivity extends ActionBarActivity {

    protected Spinner sQuantity;
    protected TextView tvStockDetailName;
    protected TextView tvStockDetailPrice;
    protected TextView tvStockDetailDescription;
    protected ImageView ivStockDetailImage;

    private DecimalFormat df = new DecimalFormat("#.00");
    private String[] spinnerQuantity;
    private Stock stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        this.spinnerQuantity = new String[] {
                "100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"
        };
        sQuantity = (Spinner) findViewById(R.id.sQuantityToBuy);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getApplication(), android.R.layout.simple_spinner_item, spinnerQuantity);
        sQuantity.setAdapter(adapter);

        tvStockDetailName = (TextView) findViewById(R.id.tvStockDetailName);
        tvStockDetailPrice = (TextView) findViewById(R.id.tvStockDetailPrice);
        tvStockDetailDescription = (TextView) findViewById(R.id.tvStockDetailDescription);
        ivStockDetailImage = (ImageView) findViewById(R.id.ivStockDetailImage);

        Intent i = getIntent();
        stock = new Stock(
                i.getStringExtra("objectID"),
                i.getStringExtra("stockID"),
                i.getStringExtra("name"),
                i.getDoubleExtra("price", 0),
                i.getIntExtra("quantity", 0),
                i.getStringExtra("description")
        );

        tvStockDetailName.setText(stock.getName());
        tvStockDetailPrice.setText("RM " + df.format(stock.getPrice()));
        tvStockDetailDescription.setText(stock.getDescription());

        //Load the selected stock's image.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentreStock");
        query.getInBackground(stock.getObjectID(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Picasso.with(getApplication().getApplicationContext()).load(object.getParseFile("Image").getUrl()).noFade().into(ivStockDetailImage);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_detail, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    public void addToCart(View v){
        Cart cart = CartHelper.getCart();
        cart.add(stock, Integer.valueOf(sQuantity.getSelectedItem().toString()));
        Intent intent = new Intent(StockDetailActivity.this, ShoppingCartActivity.class);
        startActivity(intent);
        finish();
    }
}
