package com.example.lenovo.osc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


public class StockProfileActivity extends ActionBarActivity {

    protected Spinner sStockProfileCategory;
    protected ImageView ivStockProfileImage;
    protected TextView tvStockProfileID;
    protected EditText tfStockProfileName;
    protected EditText tfStockProfileCost;
    protected EditText tfStockProfilePrice;
    protected EditText tfStockProfileQuantity;
    protected EditText tfStockProfileLocation;
    protected EditText tfStockProfileDescription;

    private DecimalFormat df = new DecimalFormat("#.00");
    private String[] spinnerCategory;
    private String objectID;
    private String stockID;
    private String name;
    private String category;
    private Double cost;
    private Double price;
    private int quantity;
    private String location;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_profile);

        ivStockProfileImage = (ImageView) findViewById(R.id.ivStockProfileImage);
        sStockProfileCategory = (Spinner) findViewById(R.id.sStockProfileCategory);
        tvStockProfileID = (TextView) findViewById(R.id.tvStockProfileID);
        tfStockProfileName = (EditText) findViewById(R.id.tfStockProfileName);
        tfStockProfileCost = (EditText) findViewById(R.id.tfStockProfileCost);
        tfStockProfilePrice = (EditText) findViewById(R.id.tfStockProfilePrice);
        tfStockProfileQuantity = (EditText) findViewById(R.id.tfStockProfileQuantity);
        tfStockProfileLocation = (EditText) findViewById(R.id.tfStockProfileLocation);
        tfStockProfileDescription = (EditText) findViewById(R.id.tfStockProfileDescription);

        this.spinnerCategory = new String[] {
                "Category", "Phone", "Tablet", "Laptop", "Mouse", "Keyboard", "Headphone", "Speaker",
                "Console", "Processor", "Other"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerCategory);
        sStockProfileCategory.setAdapter(adapter);

        Intent i = getIntent();
        objectID = i.getStringExtra("objectID");
        stockID = i.getStringExtra("stockID");
        name = i.getStringExtra("name");
        category = i.getStringExtra("category");
        cost = i.getDoubleExtra("cost", 0);
        price = i.getDoubleExtra("price", 0);
        quantity = i.getIntExtra("quantity", 0);
        location = i.getStringExtra("location");
        description = i.getStringExtra("description");

        int position = adapter.getPosition(category);
        sStockProfileCategory.setSelection(position);

        tvStockProfileID.setText(stockID);
        tfStockProfileName.setText(name);
        tfStockProfileCost.setText(df.format(cost));
        tfStockProfilePrice.setText(df.format(price));
        tfStockProfileQuantity.setText(String.valueOf(quantity));
        tfStockProfileLocation.setText(location);
        tfStockProfileDescription.setText(description);

        //Load the selected stock's image.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentreStock");
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Picasso.with(getBaseContext().getApplicationContext()).load(object.getParseFile("Image").getUrl()).noFade().into(ivStockProfileImage);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
