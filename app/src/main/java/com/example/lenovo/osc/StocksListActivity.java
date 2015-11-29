package com.example.lenovo.osc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.osc.Stocks.StockAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class StocksListActivity extends ActionBarActivity {

    protected ListView lvMakeOrderStock;
    protected ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks_list);

        lvMakeOrderStock = (ListView)findViewById(R.id.lvMakeOrderStock);
        new LoadStockFromParse().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_make_order, menu);
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

    /**
     * Load the centre stock data from parse.com
     */
    private class LoadStockFromParse extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(StocksListActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("OSC");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        /**
         * Get stock data from parse.com
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {

            mProgressDialog.dismiss();
            ParseQuery<ParseObject> stockQuery = new ParseQuery<ParseObject>("CentreStock");
            stockQuery.include("SupplierObjectId");
            stockQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> objects, ParseException e) {
                    if (e == null) {

                        StockAdapter adapterStock = new StockAdapter(StocksListActivity.this, objects);
                        lvMakeOrderStock.setAdapter(adapterStock);
                        lvMakeOrderStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Send selected stock data to StockProfileActivity.
                                Intent i = new Intent(StocksListActivity.this, StockProfileActivity.class);

                                i.putExtra("objectID", objects.get(position).getObjectId());
                                i.putExtra("stockID", objects.get(position).getString("CentreStockID"));
                                i.putExtra("name", objects.get(position).getString("Name"));
                                i.putExtra("category", objects.get(position).getString("Category"));
                                i.putExtra("cost", objects.get(position).getDouble("Cost"));
                                i.putExtra("price", objects.get(position).getDouble("Price"));
                                i.putExtra("quantity", objects.get(position).getInt("Quantity"));
                                i.putExtra("description", objects.get(position).getString("Description"));
                                i.putExtra("location", objects.get(position).getString("Location"));
                                i.putExtra("supplierName", objects.get(position).getParseObject("SupplierObjectId").getString("Name"));
                                i.putExtra("status", objects.get(position).getString("SaleStatus"));
                                startActivity(i);
                            }
                        });
                    } else {
                        Toast.makeText(StocksListActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            return null;
        }
    }
}
