package com.example.lenovo.osc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;


public class StockProfileActivity extends ActionBarActivity {

    //Declaration of components and variables
    protected Spinner sStockProfileCategory;
    protected ImageView ivStockProfileImage;
    protected TextView tvStockProfileID;
    protected TextView tvStockProfileStatus;
    protected EditText tfStockProfileName;
    protected EditText tfStockProfileCost;
    protected EditText tfStockProfilePrice;
    protected EditText tfStockProfileQuantity;
    protected EditText tfStockProfileLocation;
    protected EditText tfStockProfileDescription;
    protected EditText tfStockProfileSupplierName;
    protected Button bEdit;
    protected Button bRemove;
    protected Button bOrder;
    protected Button bUpdate;
    protected Button bCancel;

    private DecimalFormat df = new DecimalFormat("#.00");
    private String[] spinnerCategory;
    private String objectID;
    private String stockID;
    private String name;
    private String category;
    private Double cost;
    private Double price;
    private int quantity;
    private String location;
    private String description;
    private String supplierName;
    private String status;
    private String updateSupplierName;
    private String updateSupplierObjectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_profile);

        ivStockProfileImage = (ImageView) findViewById(R.id.ivStockProfileImage);
        sStockProfileCategory = (Spinner) findViewById(R.id.sStockProfileCategory);
        tvStockProfileID = (TextView) findViewById(R.id.tvStockProfileID);
        tvStockProfileStatus = (TextView) findViewById(R.id.tvStockProfileStatus);
        tfStockProfileName = (EditText) findViewById(R.id.tfStockProfileName);
        tfStockProfileCost = (EditText) findViewById(R.id.tfStockProfileCost);
        tfStockProfilePrice = (EditText) findViewById(R.id.tfStockProfilePrice);
        tfStockProfileQuantity = (EditText) findViewById(R.id.tfStockProfileQuantity);
        tfStockProfileLocation = (EditText) findViewById(R.id.tfStockProfileLocation);
        tfStockProfileDescription = (EditText) findViewById(R.id.tfStockProfileDescription);
        tfStockProfileSupplierName = (EditText) findViewById(R.id.tfStockProfileSupplierName);

        selectSupplier();

        bEdit = (Button)  findViewById(R.id.bEditStock);
        bCancel = (Button) findViewById(R.id.bCancelUpdateStock);
        bUpdate = (Button) findViewById(R.id.bUpdateStock);
        bRemove = (Button) findViewById(R.id.bRemoveStock);
        bOrder = (Button) findViewById(R.id.bOrderStock);

        this.spinnerCategory = new String[] {
                "Category", "Phone", "Tablet", "Laptop", "Mouse", "Keyboard", "Headphone", "Speaker",
                "Console", "Processor", "Other"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerCategory);
        sStockProfileCategory.setAdapter(adapter);

        //Get data from previous activity
        Intent i = getIntent();
        objectID = i.getStringExtra("objectID");
        stockID = i.getStringExtra("stockID");
        name = i.getStringExtra("name");
        category = i.getStringExtra("category");
        cost = i.getDoubleExtra("cost", 0);
        price = i.getDoubleExtra("price", 0);
        quantity = i.getIntExtra("quantity", 0);
        location = i.getStringExtra("location");
        description = i.getStringExtra("description");
        supplierName = i.getStringExtra("supplierName");
        status = i.getStringExtra("status");

        //Set text
        int position = adapter.getPosition(category);
        sStockProfileCategory.setSelection(position);
        tvStockProfileID.setText(stockID);
        tvStockProfileStatus.setText(status);
        tfStockProfileName.setText(name);
        tfStockProfileCost.setText(df.format(cost));
        tfStockProfilePrice.setText(df.format(price));
        tfStockProfileQuantity.setText(String.valueOf(quantity));
        tfStockProfileLocation.setText(location);
        tfStockProfileDescription.setText(description);
        tfStockProfileSupplierName.setText(supplierName);

        //Load the selected stock's image.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentreStock");
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Picasso.with(getBaseContext().getApplicationContext()).load(object.getParseFile("Image").getUrl()).noFade().into(ivStockProfileImage);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_profile, menu);
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
     * Enable edit selected stock.
     */
    public void editStock(){
        sStockProfileCategory.setFocusableInTouchMode(true);
        tfStockProfileName.setFocusableInTouchMode(true);
        tfStockProfileCost.setFocusableInTouchMode(true);
        tfStockProfilePrice.setFocusableInTouchMode(true);
        tfStockProfileQuantity.setFocusableInTouchMode(true);
        tfStockProfileLocation.setFocusableInTouchMode(true);
        tfStockProfileDescription.setFocusableInTouchMode(true);

        bCancel.setVisibility(View.VISIBLE);
        bEdit.setVisibility(View.INVISIBLE);
        bRemove.setVisibility(View.INVISIBLE);
        bOrder.setVisibility(View.INVISIBLE);
    }

    /**
     * Cancel update stock's data.
     */
    public void cancelUpdateStock(View v){
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    /**
     * Update selected stock's current data.
     */
//    public void updateStock(View v){
//
//        String updateName;
//        String updateCategory;
//        String updateCost;
//        String updatePrice;
//        String updateQuantity;
//        String updateLocation;
//        String updateDescription;
//        String updateSupplierObjectId;
//
//        updateName = tfStockProfileName.getText().toString();
//        updateCategory = sStockProfileCategory.getSelectedItem().toString();
//        updateCost = tfStockProfileCost.getText().toString();
//        updatePrice = tfStockProfilePrice.getText().toString();
//        updateQuantity = tfStockProfileQuantity.getText().toString();
//        updateLocation = tfStockProfileLocation.getText().toString();
//        updateDescription = tfStockProfileDescription.getText().toString();
//        updateSupplierObjectId = getIntent().getStringExtra("SupplierObjectId");
//
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentreStock");
//        query.getInBackground(objectID, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject stock, ParseException e) {
//                if (e == null){
//                    stock.put("Name", updateName);
//                    stock.put("Category", updateCategory);
//                    stock.put("Cost", Double.parseDouble(updateCost));
//                    stock.put("Price", Double.parseDouble(updatePrice));
//                    stock.put("Quantity", Integer.parseInt(updateQuantity));
//                    stock.put("Location", updateLocation);
//                    stock.put("Description", updateDescription);
//                    ParseObject product = ParseObject.createWithoutData("Supplier", updateSupplierObjectId);
//                    stock.put("SupplierObjectId", product);
//                }
//            }
//        });
//
//    }

    /**
     * Remove selected stock from sale
     */
    public void removeStockFromSale(View v){

        if (status.equalsIgnoreCase("Not For Sale")){
            Toast.makeText(getApplicationContext(),"Stock has been removed from sale.", Toast.LENGTH_SHORT).show();
        } else {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Do your Yes progress
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("CentreStock");
                            query.getInBackground(objectID, new GetCallback<ParseObject>() {
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        object.put("Status", "Not For Sale");
                                        object.saveInBackground();

                                        tvStockProfileStatus.setText("Not For Sale");
                                        Toast.makeText(getApplicationContext(),"Stock removed from sale.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //Do your No progress
                            break;
                    }
                }
            };
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setMessage("Are you sure to remove from sale?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }

    /**
     * Proceed to order stock process
     */
    public void orderStock(View v){

    }

    public void selectSupplier() {

        tfStockProfileSupplierName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Supplier");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(final List<ParseObject> supplierList, ParseException e) {
                        if (e == null) {
                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(StockProfileActivity.this);
                            builderSingle.setTitle("Select a supplier:");

                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                    StockProfileActivity.this, android.R.layout.select_dialog_item);

                            for (int i = 0; i < supplierList.size(); i++) {
                                arrayAdapter.add(supplierList.get(i).getString("Name") + " , "
                                        + supplierList.get(i).getString("Company"));
                            }

                            builderSingle.setNegativeButton(
                                    "cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                            builderSingle.setAdapter(
                                    arrayAdapter,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateSupplierObjectID = supplierList.get(which).getObjectId();
                                            updateSupplierName = supplierList.get(which).getString("Name");
                                            tfStockProfileSupplierName.setText(updateSupplierName);
                                        }
                                    });
                            builderSingle.create();
                            builderSingle.show();
                        } else {
                            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
