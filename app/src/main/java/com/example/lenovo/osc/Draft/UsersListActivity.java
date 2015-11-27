//package com.example.lenovo.osc;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//import com.example.lenovo.osc.Users.User;
//import com.example.lenovo.osc.Users.UserAdapter;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class UsersListActivity extends ActionBarActivity {
//    // Declare Variables
//    protected ListView lvStaff;
//    protected ListView lvSupplier;
//    protected ListView lvStockist;
//    protected List<ParseObject> staffList;
//    protected List<ParseObject> supplierList;
//    protected List<ParseObject> stockistList;
//    protected ProgressDialog mProgressDialog;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_users_list);
//        // Execute RemoteDataTask AsyncTask
//        new RemoteDataTask().execute();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    // RemoteDataTask AsyncTask
//    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
//        ArrayList<User> staffs = new ArrayList<>();
//        ArrayList<User> suppliers = new ArrayList<>();
//        ArrayList<User> stockists = new ArrayList<>();
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Create a progressdialog
//            mProgressDialog = new ProgressDialog(UsersListActivity.this);
//            // Set progressdialog title
//            mProgressDialog.setTitle("OSC");
//            // Set progressdialog message
//            mProgressDialog.setMessage("Loading...");
//            mProgressDialog.setIndeterminate(false);
//            // Show progressdialog
//            mProgressDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            // Locate the class table named "Staff" in Parse.com
//            ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>(
//                    "Staff");
//            try {
//                staffList = query1.find();
//            } catch (com.parse.ParseException e) {
//                e.printStackTrace();
//            }
//
//            // Locate the class table named "Supplier" in Parse.com
//            ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>(
//                    "Supplier");
//            try {
//                supplierList = query2.find();
//            } catch (com.parse.ParseException e) {
//                e.printStackTrace();
//            }
//
//            // Locate the class table named "Stockist" in Parse.com
//            ParseQuery<ParseObject> query3 = new ParseQuery<ParseObject>(
//                    "Stockist");
//            try {
//                stockistList = query3.find();
//            } catch (com.parse.ParseException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            // Close the progressdialog
//            mProgressDialog.dismiss();
//
//            //Display Staff=======================================================================
//            lvStaff = (ListView) findViewById(R.id.lvStaff);
//            // Pass the results into an ArrayAdapter
//            UserAdapter adapterStaff = new UserAdapter(UsersListActivity.this, generateStaffData());
//            // Binds the Adapter to the ListView
//            lvStaff.setAdapter(adapterStaff);
//            // Capture button clicks on ListView items
//            lvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    // Send single item click data to UserProfileActivity Class
//                    Intent i = new Intent(UsersListActivity.this,
//                            UserProfileActivity.class);
//                    // Pass data "name" followed by the position
//                    i.putExtra("objectID", staffList.get(position).getObjectId());
//                    i.putExtra("userID", staffList.get(position).getString("StaffID"));
//                    i.putExtra("name", staffList.get(position).getString("Name"));
//                    i.putExtra("ic", staffList.get(position).getString("IC"));
//                    i.putExtra("tel", staffList.get(position).getString("Tel"));
//                    i.putExtra("email", staffList.get(position).getString("Email"));
//                    i.putExtra("address", staffList.get(position).getString("Address"));
//                    i.putExtra("status", staffList.get(position).getString("Status"));
//
//                    // Open UserProfileActivity.java Activity
//                    startActivity(i);
//                }
//            });
//
//            //Display Supplier=====================================================================
//            lvSupplier = (ListView) findViewById(R.id.lvSupplier);
//            // Pass the results into an ArrayAdapter
//            UserAdapter adapterSupplier = new UserAdapter(UsersListActivity.this, generateSupplierData());
//            // Binds the Adapter to the ListView
//            lvSupplier.setAdapter(adapterSupplier);
//            // Capture button clicks on ListView items
//            lvSupplier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    // Send single item click data to UserProfileActivity Class
//                    Intent i = new Intent(UsersListActivity.this,
//                            UserProfileActivity.class);
//                    // Pass data "name" followed by the position
//                    i.putExtra("objectID", supplierList.get(position).getObjectId());
//                    i.putExtra("userID", supplierList.get(position).getString("SupplierID"));
//                    i.putExtra("name", supplierList.get(position).getString("Name"));
//                    i.putExtra("ic", supplierList.get(position).getString("IC"));
//                    i.putExtra("tel", supplierList.get(position).getString("Tel"));
//                    i.putExtra("email", supplierList.get(position).getString("Email"));
//                    i.putExtra("address", supplierList.get(position).getString("Address"));
//                    i.putExtra("company", supplierList.get(position).getString("Company"));
//                    i.putExtra("status", supplierList.get(position).getString("Status"));
//
//                    // Open UserProfileActivity.java Activity
//                    startActivity(i);
//                }
//            });
//
//            //Display Supplier=====================================================================
//            lvStockist = (ListView) findViewById(R.id.lvStockist);
//            // Pass the results into an ArrayAdapter
//            UserAdapter adapterStockist = new UserAdapter(UsersListActivity.this, generateStockistData());
//            // Binds the Adapter to the ListView
//            lvStockist.setAdapter(adapterStockist);
//            // Capture button clicks on ListView items
//            lvStockist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    // Send single item click data to UserProfileActivity Class
//                    Intent i = new Intent(UsersListActivity.this,
//                            UserProfileActivity.class);
//                    // Pass data "name" followed by the position
//                    i.putExtra("objectID", stockistList.get(position).getObjectId());
//                    i.putExtra("userID", stockistList.get(position).getString("StockistID"));
//                    i.putExtra("name", stockistList.get(position).getString("Name"));
//                    i.putExtra("ic", stockistList.get(position).getString("IC"));
//                    i.putExtra("tel", stockistList.get(position).getString("Tel"));
//                    i.putExtra("email", stockistList.get(position).getString("Email"));
//                    i.putExtra("address", stockistList.get(position).getString("Address"));
//                    i.putExtra("status", stockistList.get(position).getString("Status"));
//
//                    // Open UserProfileActivity.java Activity
//                    startActivity(i);
//                }
//            });
//        }
//
//        private ArrayList<User> generateStaffData() {
//            for (ParseObject user : staffList) {
//                staffs.add(new User((String) user.get("objectId"), (String) user.get("StaffID"), (String) user.get("Name")));
//            }
//            return staffs;
//        }
//
//        private ArrayList<User> generateSupplierData() {
//            for (ParseObject user : supplierList) {
//                suppliers.add(new User((String) user.get("objectId"), (String) user.get("SupplierID"), (String) user.get("Name")));
//            }
//            return suppliers;
//        }
//
//        private ArrayList<User> generateStockistData() {
//            for (ParseObject user : stockistList) {
//                stockists.add(new User((String) user.get("objectId"), (String) user.get("StockistID"), (String) user.get("Name")));
//            }
//            return stockists;
//        }
//    }
//
//    public void showStaff(View view){
//        lvStaff.setVisibility(View.VISIBLE);
//        lvSupplier.setVisibility(View.INVISIBLE);
//        lvStockist.setVisibility(View.INVISIBLE);
//    }
//
//    public void showSupplier(View view){
//        lvStaff.setVisibility(View.INVISIBLE);
//        lvSupplier.setVisibility(View.VISIBLE);
//        lvStockist.setVisibility(View.INVISIBLE);
//    }
//
//    public void showStockist(View view){
//        lvStaff.setVisibility(View.INVISIBLE);
//        lvSupplier.setVisibility(View.INVISIBLE);
//        lvStockist.setVisibility(View.VISIBLE);
//    }
//}
