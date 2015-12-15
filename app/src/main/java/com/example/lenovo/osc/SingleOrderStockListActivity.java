package com.example.lenovo.osc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.osc.Main.LoginActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;


public class SingleOrderStockListActivity extends ActionBarActivity {


    protected ListView lvOrderStock;
    protected TextView tvTotalPrice;
    protected Button bOk;

    private ParseRelation items;
    private DecimalFormat df = new DecimalFormat("#.00");
    private String objectID;
    private List quantity;
    private Date receiveDate;
    private Date deliverDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order_stock_list);

        lvOrderStock = (ListView) findViewById(R.id.lvOrderStockList);
        tvTotalPrice = (TextView) findViewById(R.id.tvSingleOrderStockTotalPrice);
        bOk = (Button) findViewById(R.id.bSingleOrderStockOk);

        Intent i = getIntent();
        objectID = i.getStringExtra("objectId");

        ParseQuery query = ParseQuery.getQuery("Sale");
        query.include("Quantity");
        query.include("StaffObjectID");
        query.include("StockistObjectID");
        if (LoginActivity.currentUser.getUserID().charAt(0) != 'S'){
            query.whereEqualTo("StockistObjectID", LoginActivity.currentUser.getObjectID());
        }
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                quantity = object.getList("Quantity");
                receiveDate = object.getDate("ReceiveDate");
                deliverDate = object.getDate("DeliverDate");
                tvTotalPrice.setText("Total Price : RM" + df.format(object.getNumber("TotalPrice")));
                if (deliverDate == null && LoginActivity.currentUser.getUserID().charAt(0) == 'S'){
                    bOk.setText("Deliver");
                } else if(receiveDate == null && LoginActivity.currentUser.getUserID().charAt(0) == 'T'){
                    bOk.setText("Receive");
                }

                items = object.getRelation("CentreStock");
                ParseQuery query1 = items.getQuery();
                query1.findInBackground(new FindCallback<ParseObject>(){
                    @Override
                    public void done(final List<ParseObject> objects, ParseException e) {
                        OrderStockAdapter orderStockAdapter =
                                new OrderStockAdapter(SingleOrderStockListActivity.this, objects, quantity);
                        lvOrderStock.setAdapter(orderStockAdapter);
                        lvOrderStock.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getApplication(), objects.get(position).getString("Name").toString(),
                                        Toast.LENGTH_SHORT);
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
        menu.findItem(R.id.action_settings).setVisible(false);
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

    public void update(View v){
        if (deliverDate == null && LoginActivity.currentUser.getUserID().charAt(0) == 'S'){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Sale");
            query.getInBackground(objectID, new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject object, ParseException e) {
                    if (e == null) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Staff");
                        query.getInBackground(LoginActivity.currentUser.getObjectID(),
                                new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject staff, ParseException e) {
                                        object.put("DeliverDate", new Date());
                                        object.put("StaffObjectID", staff);
                                        object.saveInBackground();
                                        Toast.makeText(getApplicationContext(), "Order delivered.",
                                                Toast.LENGTH_SHORT).show();
                                        bOk.setText("Ok");
                                        finish();
                                    }

                                });
                    }
                };
            });

        } else if (receiveDate == null && LoginActivity.currentUser.getUserID().charAt(0) == 'T'){

            if(deliverDate == null){
                Toast.makeText(getApplication(), "Ordered stocks are not delivered yet.",
                        Toast.LENGTH_SHORT).show();
            } else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Sale");
                query.getInBackground(objectID, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            Date date = new Date();
                            object.put("ReceiveDate", date);
                            object.saveInBackground();
                            Toast.makeText(getApplicationContext(), "Order received.",
                                    Toast.LENGTH_SHORT).show();
                            bOk.setText("Ok");
                            finish();
                        }
                    }
                });
            }

        } else
            finish();
    }
}

