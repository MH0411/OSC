package com.example.lenovo.osc.StockistFunction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.lenovo.osc.R;
import com.example.lenovo.osc.Stocks.StockOnSaleAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Lenovo on 10/12/2015.
 */
public class StocksOnSaleFragment extends Fragment {

    protected GridView gvStocksList;
    protected ProgressDialog mProgressDialog;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.stock_on_sale_layout, container, false);

        gvStocksList = (GridView) view.findViewById(R.id.gvStockOnSaleList);
        new LoadStockFromParse().execute();

        return view;
    }

    /**
     * Load the centre stock data from parse.com
     */
    private class LoadStockFromParse extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
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
            if (determineCategory() != null)
                stockQuery.whereEqualTo("Category", determineCategory());
            stockQuery.whereEqualTo("SaleStatus", "On Sale");
            stockQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> objects, ParseException e) {
                    if (e == null) {

                        StockOnSaleAdapter adapterStock = new StockOnSaleAdapter(getActivity(), objects);
                        gvStocksList.setAdapter(adapterStock);
                        gvStocksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Send selected stock data to StockProfileFragment.

                                Intent i = new Intent(getActivity(), StockDetailActivity.class);

                                i.putExtra("objectID", objects.get(position).getObjectId());
                                i.putExtra("stockID", objects.get(position).getString("CentreStockID"));
                                i.putExtra("name", objects.get(position).getString("Name"));
                                i.putExtra("price", objects.get(position).getDouble("Price"));
                                i.putExtra("quantity", objects.get(position).getInt("Quantity"));
                                i.putExtra("description", objects.get(position).getString("Description"));

                                startActivity(i);
                            }
                        });
                    }
                }
            });
            return null;
        }
    }

    /**
     * Determine which category user choose
     */
    private String determineCategory(){
        int position = getArguments().getInt("position", 0);
        String category = null;
        if (position == 0)
            category = null;
        else if (position == 1)
            category = "Phone";
        else if (position == 2)
            category = "Tablet";
        else if (position == 3)
            category = "Mouse";
        else if (position == 4)
            category = "Keyboard";
        else if (position == 5)
            category = "Headphone";
        else if (position == 6)
            category = "Speaker";
        else if (position == 7)
            category = "Console";
        else if (position == 8)
            category = "Processor";
        else if (position == 9)
            category = "Other";
        return category;
    }
}
