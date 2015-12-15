package com.example.lenovo.osc;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.osc.Main.LoginActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Lenovo on 14/12/2015.
 */
public class StockistOrderListFragment extends Fragment {

    protected ListView lvSale;
    protected TextView tvSaleID;
    protected TextView tvDate;
    protected ProgressDialog mProgressDialog;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sale_list_layout, container, false);

        lvSale = (ListView) view.findViewById(R.id.lvSale);
        tvSaleID = (TextView) view.findViewById(R.id.tvSaleID);
        tvDate = (TextView) view.findViewById(R.id.tvSaleDate);

        new LoadFromParse().execute();

        return view;
    }

    private class LoadFromParse extends AsyncTask<Void, Void, Void> {

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

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();

            ParseQuery<ParseObject> orderQuery = new ParseQuery<ParseObject>("Sale");
            if (LoginActivity.currentUser.getUserID().charAt(0) != 'S'){
                ParseObject object = ParseObject.createWithoutData("Stockist",
                        LoginActivity.currentUser.getObjectID());
                orderQuery.whereEqualTo("StockistObjectID", object);
            }
            orderQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        SaleAdapter adapterSale = new SaleAdapter(getActivity(), objects);
                        lvSale.setAdapter(adapterSale);
                        lvSale.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent i = new Intent(getActivity(), SingleOrderStockListActivity.class);
                                i.putExtra("objectId", objects.get(position).getObjectId());
                                startActivity(i);

                            }
                        });
                    } else
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
