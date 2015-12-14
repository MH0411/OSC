package com.example.lenovo.osc.AdminFragment;

import android.app.ProgressDialog;
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

import com.example.lenovo.osc.Order.OrderAdapter;
import com.example.lenovo.osc.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Lenovo on 30/11/2015.
 */
public class OrdersListFragment extends Fragment {

    protected ListView lvOrderList;
    protected TextView tvOrderStockName;
    protected TextView tvOrderQuantity;
    protected ProgressDialog mProgressDialog;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.orders_list_layout, container, false);

        lvOrderList = (ListView) view.findViewById(R.id.lvOrderList);
        tvOrderStockName = (TextView) view.findViewById(R.id.tvStockName);
        tvOrderQuantity = (TextView) view.findViewById(R.id.tfOrderQuantity);

        new RemoteDataTask().execute();

        return view;
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void>{

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
            mProgressDialog.dismiss();

            ParseQuery<ParseObject> orderQuery = new ParseQuery<ParseObject>("CentreOrder");
            orderQuery.include("SupplierObjectId");
            orderQuery.include("CentreStockObjectID");
            orderQuery.include("StaffObjectID");
            orderQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> objects, final ParseException e) {
                    if (e == null) {

                        OrderAdapter adapterOrder = new OrderAdapter(getActivity(), objects);
                        lvOrderList.setAdapter(adapterOrder);
                        lvOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Send selected stock data to OrderProfileFragment
                                Bundle bundle = new Bundle();
                                bundle.putString("objectID", objects.get(position).getObjectId());
                                bundle.putString("name", objects.get(position).
                                        getParseObject("CentreStockObjectID").getString("Name"));
                                bundle.putInt("quantity", objects.get(position).getInt("Quantity"));
                                bundle.putDouble("amount", objects.get(position).getInt("Amount"));
                                bundle.putString("orderDate", objects.get(position).
                                        getDate("OrderDate").toString());
                                if (objects.get(position).getDate("ReceiveDate") != null)
                                    bundle.putString("receiveDate", objects.get(position).
                                            getDate("ReceiveDate").toString());
                                else
                                    bundle.putString("receiveDate", null);

                                OrderProfileFragment opf = new OrderProfileFragment();
                                opf.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.container, opf).
                                        addToBackStack(null).commit();
                            }
                        });
                    }
                }
            });
        }
    }
}
