package com.example.lenovo.osc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.lenovo.osc.Main.LoginActivity;
import com.example.lenovo.osc.Stocks.StockAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class SingleOrderStockListActivity extends ActionBarActivity {

    private String objectID;
    protected ListView lvOrderStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order_stock_list);

        lvOrderStock = (ListView) findViewById(R.id.lvOrderStockList);

        Intent i = getIntent();
        objectID = i.getStringExtra("objectId");

        ParseQuery query = ParseQuery.getQuery("Sale");
        query.include("StockistObjectID");
        query.include("Stocks");
        query.include("Quantity");
        query.whereEqualTo("StockistObjectID", LoginActivity.currentUser.getObjectID());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null) {
                    StockAdapter stockAdapter = new StockAdapter(getApplication(), objects);
                    lvOrderStock.setAdapter(stockAdapter);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_order_stock_list, menu);
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
