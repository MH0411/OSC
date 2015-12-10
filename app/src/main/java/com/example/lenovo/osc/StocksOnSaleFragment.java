package com.example.lenovo.osc;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
                                Bundle bundle = new Bundle();

                                bundle.putString("objectID", objects.get(position).getObjectId());
                                bundle.putString("stockID", objects.get(position).getString("CentreStockID"));
                                bundle.putString("name", objects.get(position).getString("Name"));
                                bundle.putString("category", objects.get(position).getString("Category"));
                                bundle.putDouble("price", objects.get(position).getDouble("Price"));
                                bundle.putInt("quantity", objects.get(position).getInt("Quantity"));
                                bundle.putString("description", objects.get(position).getString("Description"));
                                bundle.putString("status", objects.get(position).getString("SaleStatus"));
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
//
//                                StockProfileFragment spf = new StockProfileFragment();
//                                spf.setArguments(bundle);
//                                getFragmentManager().beginTransaction().replace(R.id.container, spf).addToBackStack(null).commit();
                            }
                        });
                    }
                }
            });
            return null;
        }
    }
}
