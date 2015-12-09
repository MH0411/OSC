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
import android.widget.Button;
import android.widget.ListView;

import com.example.lenovo.osc.R;
import com.example.lenovo.osc.Users.User;
import com.example.lenovo.osc.Users.UserAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 24/11/2015.
 */
public class UpdateUserFragment extends Fragment implements View.OnClickListener {

    // Declare Variables
    protected ListView lvStaff;
    protected ListView lvSupplier;
    protected ListView lvStockist;
    protected Button bStaff;
    protected Button bSupplier;
    protected Button bStockist;
    protected ProgressDialog mProgressDialog;

    private List<ParseObject> staffList;
    private List<ParseObject> supplierList;
    private List<ParseObject> stockistList;

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_users_list, container, false);

        lvStaff = (ListView) myView.findViewById(R.id.lvStaff);
        lvSupplier = (ListView) myView.findViewById(R.id.lvSupplier);
        lvStockist = (ListView) myView.findViewById(R.id.lvStockist);

        bStaff = (Button) myView.findViewById(R.id.bShowStaff);
        bSupplier = (Button) myView.findViewById(R.id.bShowSupplier);
        bStockist = (Button) myView.findViewById(R.id.bShowStockist);
        bStaff.setOnClickListener(this);
        bSupplier.setOnClickListener(this);
        bStockist.setOnClickListener(this);

        new RemoteDataTask().execute();
        return myView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bShowStaff:
                lvStaff.setVisibility(View.VISIBLE);
                lvSupplier.setVisibility(View.INVISIBLE);
                lvStockist.setVisibility(View.INVISIBLE);
                break;

            case R.id.bShowSupplier:
                lvStaff.setVisibility(View.INVISIBLE);
                lvSupplier.setVisibility(View.VISIBLE);
                lvStockist.setVisibility(View.INVISIBLE);
                break;

            case R.id.bShowStockist:
                lvStaff.setVisibility(View.INVISIBLE);
                lvSupplier.setVisibility(View.INVISIBLE);
                lvStockist.setVisibility(View.VISIBLE);
                break;

        }
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        ArrayList<User> staffs = new ArrayList<>();
        ArrayList<User> suppliers = new ArrayList<>();
        ArrayList<User> stockists = new ArrayList<>();

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
            // Locate the class table named "Staff" in Parse.com
            ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>(
                    "Staff");
            try {
                staffList = query1.find();
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }

            // Locate the class table named "Supplier" in Parse.com
            ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>(
                    "Supplier");
            try {
                supplierList = query2.find();
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }

            // Locate the class table named "Stockist" in Parse.com
            ParseQuery<ParseObject> query3 = new ParseQuery<ParseObject>(
                    "Stockist");
            try {
                stockistList = query3.find();
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Close the progressdialog
            mProgressDialog.dismiss();

            //Display Staff=======================================================================
            lvStaff = (ListView) myView.findViewById(R.id.lvStaff);
            // Pass the results into an ArrayAdapter
            UserAdapter adapterStaff = new UserAdapter(UpdateUserFragment.this, generateStaffData());
            // Binds the Adapter to the ListView
            lvStaff.setAdapter(adapterStaff);
            // Capture button clicks on ListView items
            lvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to UserProfileFragment Class
                    Bundle bundle = new Bundle();
                    bundle.putString("objectID", staffList.get(position).getObjectId());
                    bundle.putString("userID", staffList.get(position).getString("StaffID"));
                    bundle.putString("name", staffList.get(position).getString("Name"));
                    bundle.putString("ic", staffList.get(position).getString("IC"));
                    bundle.putString("tel", staffList.get(position).getString("Tel"));
                    bundle.putString("email", staffList.get(position).getString("Email"));
                    bundle.putString("address", staffList.get(position).getString("Address"));
                    bundle.putString("status", staffList.get(position).getString("Status"));

                    UserProfileFragment upf = new UserProfileFragment();
                    upf.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.container, upf).addToBackStack(null).commit();
                }
            });

            //Display Supplier=====================================================================
            lvSupplier = (ListView) myView.findViewById(R.id.lvSupplier);
            // Pass the results into an ArrayAdapter
            UserAdapter adapterSupplier = new UserAdapter(UpdateUserFragment.this, generateSupplierData());
            // Binds the Adapter to the ListView
            lvSupplier.setAdapter(adapterSupplier);
            // Capture button clicks on ListView items
            lvSupplier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to UserProfileFragment Class
                    Bundle bundle = new Bundle();
                    bundle.putString("objectID", supplierList.get(position).getObjectId());
                    bundle.putString("userID", supplierList.get(position).getString("SupplierID"));
                    bundle.putString("name", supplierList.get(position).getString("Name"));
                    bundle.putString("ic", supplierList.get(position).getString("IC"));
                    bundle.putString("tel", supplierList.get(position).getString("Tel"));
                    bundle.putString("email", supplierList.get(position).getString("Email"));
                    bundle.putString("address", supplierList.get(position).getString("Address"));
                    bundle.putString("company", supplierList.get(position).getString("Company"));
                    bundle.putString("status", supplierList.get(position).getString("Status"));

                    UserProfileFragment upf = new UserProfileFragment();
                    upf.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.container, upf).addToBackStack(null).commit();
                }
            });

            //Display Supplier=====================================================================
            lvStockist = (ListView) myView.findViewById(R.id.lvStockist);
            // Pass the results into an ArrayAdapter
            UserAdapter adapterStockist = new UserAdapter(UpdateUserFragment.this, generateStockistData());
            // Binds the Adapter to the ListView
            lvStockist.setAdapter(adapterStockist);
            // Capture button clicks on ListView items
            lvStockist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to UserProfileFragment Class
                    Bundle bundle = new Bundle();
                    bundle.putString("objectID", stockistList.get(position).getObjectId());
                    bundle.putString("userID", stockistList.get(position).getString("StockistID"));
                    bundle.putString("name", stockistList.get(position).getString("Name"));
                    bundle.putString("ic", stockistList.get(position).getString("IC"));
                    bundle.putString("tel", stockistList.get(position).getString("Tel"));
                    bundle.putString("email", stockistList.get(position).getString("Email"));
                    bundle.putString("address", stockistList.get(position).getString("Address"));
                    bundle.putString("status", stockistList.get(position).getString("Status"));

                    UserProfileFragment upf = new UserProfileFragment();
                    upf.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.container, upf).addToBackStack(null).commit();
                }
            });
        }

        private ArrayList<User> generateStaffData() {
            for (ParseObject user : staffList) {
                staffs.add(new User((String) user.get("objectId"), (String) user.get("StaffID"), (String) user.get("Name")));
            }
            return staffs;
        }

        private ArrayList<User> generateSupplierData() {
            for (ParseObject user : supplierList) {
                suppliers.add(new User((String) user.get("objectId"), (String) user.get("SupplierID"), (String) user.get("Name")));
            }
            return suppliers;
        }

        private ArrayList<User> generateStockistData() {
            for (ParseObject user : stockistList) {
                stockists.add(new User((String) user.get("objectId"), (String) user.get("StockistID"), (String) user.get("Name")));
            }
            return stockists;
        }
    }
}
