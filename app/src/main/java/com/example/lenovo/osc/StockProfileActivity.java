package com.example.lenovo.osc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;


public class StockProfileActivity extends ActionBarActivity {

    protected ImageView ivStockProfileImage;
    protected TextView tvStockID;
    protected EditText tfStockProfileName;
    protected EditText tfStockProfileCategory;
    protected EditText tfStockProfileCost;
    protected EditText tfStockProfilePrice;
    protected EditText tfStockProfileLocation;
    protected EditText tfStockProfileDescription;

    private Uri image;
    private String objectID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_profile);

        ivStockProfileImage = (ImageView) findViewById(R.id.ivStockProfileImage);

        Intent i = getIntent();

        objectID = i.getStringExtra("objectID");

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
