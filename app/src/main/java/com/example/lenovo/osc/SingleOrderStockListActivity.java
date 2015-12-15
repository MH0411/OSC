package com.example.lenovo.osc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.osc.Main.LoginActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;


public class SingleOrderStockListActivity extends ActionBarActivity {

    private String objectID;
    protected ListView lvOrderStock;
    private ParseRelation items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order_stock_list);

        lvOrderStock = (ListView) findViewById(R.id.lvOrderStockList);

        Intent i = getIntent();
        objectID = i.getStringExtra("objectId");

        ParseQuery query = ParseQuery.getQuery("Sale");
        query.include("Quantity");
        if (LoginActivity.currentUser.getUserID().charAt(0) != 'S'){
            query.whereEqualTo("StockistObjectID", LoginActivity.currentUser.getObjectID());
        }
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                items = object.getRelation("CentreStock");
                ParseQuery query1 = items.getQuery();
                query1.findInBackground(new FindCallback<ParseObject>(){
                    @Override
                    public void done(final List<ParseObject> objects, ParseException e) {
                        OrderStockAdapter orderStockAdapter =
                                new OrderStockAdapter(SingleOrderStockListActivity.this, objects);
                        lvOrderStock.setAdapter(orderStockAdapter);
                        lvOrderStock.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getApplication(), objects.get(position).getString("Name").toString(), Toast.LENGTH_SHORT);
                            }
                        });
                    }
                });
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

