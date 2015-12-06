package com.example.lenovo.osc;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.osc.Main.LoginActivity;
import com.example.lenovo.osc.Stocks.Stock;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Lenovo on 5/12/2015.
 */
public class StockProfileFragment extends Fragment implements View.OnClickListener {

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

    //Declaration of order dialog's components
    protected TextView tfOrderQuantity;
    protected TextView tvOrderCost;
    protected EditText tfOrderAmount;

    protected ImageButton ibPlus;
    protected ImageButton ibMinus;
    protected Button bEdit;
    protected Button bRemove;
    protected Button bOrder;
    protected Button bUpdate;
    protected Button bCancel;

    private DecimalFormat df = new DecimalFormat("#.00");
    private String[] spinnerCategory;
    private String updateSupplierName;
    private String updateSupplierObjectID;
    private int orderQuantity;
    private double orderAmount;
    private Stock stock;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_stock_profile, container, false);

        ivStockProfileImage = (ImageView) view.findViewById(R.id.ivStockProfileImage);
        sStockProfileCategory = (Spinner) view.findViewById(R.id.sStockProfileCategory);
        tvStockProfileID = (TextView) view.findViewById(R.id.tvStockProfileID);
        tvStockProfileStatus = (TextView) view.findViewById(R.id.tvStockProfileStatus);
        tfStockProfileName = (EditText) view.findViewById(R.id.tfStockProfileName);
        tfStockProfileCost = (EditText) view.findViewById(R.id.tfStockProfileCost);
        tfStockProfilePrice = (EditText) view.findViewById(R.id.tfStockProfilePrice);
        tfStockProfileQuantity = (EditText) view.findViewById(R.id.tfStockProfileQuantity);
        tfStockProfileLocation = (EditText) view.findViewById(R.id.tfStockProfileLocation);
        tfStockProfileDescription = (EditText) view.findViewById(R.id.tfStockProfileDescription);
        tfStockProfileSupplierName = (EditText) view.findViewById(R.id.tfStockProfileSupplierName);

        bEdit = (Button) view.findViewById(R.id.bEditStock);
        bEdit.setOnClickListener(this);
        bCancel = (Button) view.findViewById(R.id.bCancelUpdateStock);
        bCancel.setOnClickListener(this);
        bUpdate = (Button) view.findViewById(R.id.bUpdateStock);
        bUpdate.setOnClickListener(this);
        bRemove = (Button) view.findViewById(R.id.bRemoveStock);
        bRemove.setOnClickListener(this);
        bOrder = (Button) view.findViewById(R.id.bOrderStock);
        bOrder.setOnClickListener(this);

        this.spinnerCategory = new String[]{
                "Category", "Phone", "Tablet", "Laptop", "Mouse", "Keyboard", "Headphone", "Speaker",
                "Console", "Processor", "Other"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerCategory);
        sStockProfileCategory.setAdapter(adapter);

        //Get data from previous activity
        stock = new Stock(
                getArguments().getString("objectID"),
                getArguments().getString("stockID"),
                getArguments().getString("name"),
                getArguments().getString("category"),
                getArguments().getDouble("cost", 0),
                getArguments().getDouble("price", 0),
                getArguments().getInt("quantity", 0),
                getArguments().getString("location"),
                getArguments().getString("description"),
                getArguments().getString("supplierName"),
                getArguments().getString("status")
        );

        //Set text
        int position = adapter.getPosition(stock.getCategory());
        sStockProfileCategory.setSelection(position);
        tvStockProfileID.setText(stock.getStockID());
        tvStockProfileStatus.setText(stock.getStatus());
        tfStockProfileName.setText(stock.getName());
        tfStockProfileCost.setText(df.format(stock.getCost()));
        tfStockProfilePrice.setText(df.format(stock.getPrice()));
        tfStockProfileQuantity.setText(String.valueOf(stock.getQuantity()));
        tfStockProfileLocation.setText(stock.getLocation());
        tfStockProfileDescription.setText(stock.getDescription());
        tfStockProfileSupplierName.setText(stock.getSupplierName());

        //Load the selected stock's image.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentreStock");
        query.getInBackground(stock.getObjectID(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Picasso.with(getActivity().getApplicationContext()).load(object.getParseFile("Image").getUrl()).noFade().into(ivStockProfileImage);
                    updateSupplierObjectID = object.getParseObject("SupplierObjectId").getObjectId();
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bEditStock:
                editStock();
                break;
            case R.id.bUpdateStock:
                updateStock();
                break;
            case R.id.bCancelUpdateStock:
                cancelUpdateStock();
                break;
            case R.id.bRemoveStock:
                removeStockFromSale();
                break;
            case R.id.bOrderOk:
                orderStock();
                break;
            case R.id.tfStockProfileSupplierName:
                selectSupplier();
                break;
        }
    }

    /**
     * Enable edit selected stock.
     */
    public void editStock() {
        sStockProfileCategory.setFocusableInTouchMode(true);
        tfStockProfileName.setFocusableInTouchMode(true);
        tfStockProfileCost.setFocusableInTouchMode(true);
        tfStockProfilePrice.setFocusableInTouchMode(true);
        tfStockProfileQuantity.setFocusableInTouchMode(true);
        tfStockProfileLocation.setFocusableInTouchMode(true);
        tfStockProfileDescription.setFocusableInTouchMode(true);

        bUpdate.setVisibility(View.VISIBLE);
        bCancel.setVisibility(View.VISIBLE);
        bEdit.setVisibility(View.INVISIBLE);
        bRemove.setVisibility(View.INVISIBLE);
        bOrder.setVisibility(View.INVISIBLE);
    }

    /**
     * Cancel update stock's data.
     */
    public void cancelUpdateStock() {
        Intent intent = getActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().finish();
        startActivity(intent);
    }

    /**
     * Update selected stock's current data.
     */
    public void updateStock() {

        final String updateName;
        final String updateCategory;
        final String updateCost;
        final String updatePrice;
        final String updateQuantity;
        final String updateLocation;
        final String updateDescription;

        updateName = tfStockProfileName.getText().toString();
        updateCategory = sStockProfileCategory.getSelectedItem().toString();
        updateCost = tfStockProfileCost.getText().toString();
        updatePrice = tfStockProfilePrice.getText().toString();
        updateQuantity = tfStockProfileQuantity.getText().toString();
        updateLocation = tfStockProfileLocation.getText().toString();
        updateDescription = tfStockProfileDescription.getText().toString();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentreStock");
        query.getInBackground(stock.getObjectID(), new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject stock, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Supplier");
                    query.getInBackground(updateSupplierObjectID, new GetCallback<ParseObject>() {
                        @Override
                        public void done(final ParseObject supplier, ParseException e) {
                            stock.put("Name", updateName);
                            stock.put("Category", updateCategory);
                            stock.put("Cost", Double.parseDouble(updateCost));
                            stock.put("Price", Double.parseDouble(updatePrice));
                            stock.put("Quantity", Integer.parseInt(updateQuantity));
                            stock.put("Location", updateLocation);
                            stock.put("Description", updateDescription);
                            stock.put("SupplierObjectId", supplier);
                        }
                    });

                }
            }
        });

        Toast.makeText(getActivity(), "Stock updated.", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    /**
     * Remove selected stock from sale
     */
    public void removeStockFromSale() {

        if (stock.getStatus().equalsIgnoreCase("Not For Sale")) {
            Toast.makeText(getActivity(), "Stock has been removed from sale.", Toast.LENGTH_SHORT).show();
        } else {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Do your Yes progress
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("CentreStock");
                            query.getInBackground(stock.getObjectID(), new GetCallback<ParseObject>() {
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        object.put("SaleStatus", "Not For Sale");
                                        object.saveInBackground();

                                        tvStockProfileStatus.setText("Not For Sale");
                                        Toast.makeText(getActivity(), "Stock removed from sale.", Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
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
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setMessage("Are you sure to remove from sale?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }

    /**
     * Proceed to order stock process
     */
    public void orderStock() {

        //Custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.order_dialog);
        dialog.setTitle("Order");

        int defaultQuantity = 1000;
        orderQuantity = defaultQuantity;
        tfOrderQuantity = (TextView) dialog.findViewById(R.id.tfOrderQuantity);
        tfOrderQuantity.setText(String.valueOf(defaultQuantity));

        tvOrderCost = (TextView) dialog.findViewById(R.id.tvOrderCost);
        tvOrderCost.setText("Cost: RM" + tfStockProfileCost.getText().toString());

        tfOrderAmount = (EditText) dialog.findViewById(R.id.tfOrderAmount);
        calculateAmount(defaultQuantity);
        tfOrderAmount.setText("RM " + df.format(orderAmount));

        //Add order quantity
        ibPlus = (ImageButton) dialog.findViewById(R.id.ibPlus);
        ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderQuantity = Integer.parseInt(tfOrderQuantity.getText().toString()) + 1000;
                tfOrderQuantity.setText(String.valueOf(orderQuantity));
                calculateAmount(orderQuantity);
                tfOrderAmount.setText("RM " + df.format(orderAmount));
            }
        });

        //Minus order quantity
        ibMinus = (ImageButton) dialog.findViewById(R.id.ibMinus);
        ibMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tfOrderQuantity.getText().toString().equalsIgnoreCase("0")) {
                } else {
                    orderQuantity = Integer.parseInt(tfOrderQuantity.getText().toString()) - 1000;
                    tfOrderQuantity.setText(String.valueOf(orderQuantity));
                    calculateAmount(orderQuantity);
                    tfOrderAmount.setText("RM " + df.format(orderAmount));
                }
            }
        });

        //Confirm the order
        Button dbOK = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dbOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("CentreStock");
                query1.getInBackground(stock.getObjectID(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject centreStock, ParseException e) {

                        if (e == null) {
                            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Supplier");
                            query2.getInBackground(updateSupplierObjectID, new GetCallback<ParseObject>() {
                                @Override
                                public void done(final ParseObject supplier, ParseException e) {

                                    if (e == null) {
                                        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Staff");
                                        query3.getInBackground(LoginActivity.currentUser.getObjectID(), new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject staff, ParseException e) {

                                                if (e == null) {
                                                    Toast.makeText(getActivity(), LoginActivity.currentUser.getObjectID(), Toast.LENGTH_SHORT).show();
                                                    Date date = new Date();
                                                    ParseObject order = new ParseObject("CentreOrder");
                                                    order.put("Quantity", orderQuantity);
                                                    order.put("Amount", orderAmount);
                                                    order.put("OrderDate", date);
                                                    order.put("SupplierObjectId", supplier);
                                                    order.put("StaffObjectID", staff);
                                                    order.put("CentreStockObjectID", centreStock);

                                                    //Grant permission
                                                    ParseACL acl = new ParseACL();
                                                    acl.setPublicReadAccess(true);
                                                    acl.setPublicWriteAccess(true);
                                                    order.setACL(acl);
                                                    order.saveInBackground();

                                                    dialog.dismiss();

                                                    Toast.makeText(getActivity(), "Order successful.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        //Cancel the order
        Button dbCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        dbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Select supplier from dialog.
     */
    public void selectSupplier() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Supplier");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> supplierList, ParseException e) {
                if (e == null) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                    builderSingle.setTitle("Select a supplier:");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            getActivity(), android.R.layout.select_dialog_item);

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
                }
            }
        });
    }

    /**
     * Calculate order amount
     *
     * @param orderQuantity
     */
    public void calculateAmount(int orderQuantity) {
        double currentCost = Double.parseDouble(tfStockProfileCost.getText().toString());
        orderAmount = orderQuantity * currentCost;
    }
}
