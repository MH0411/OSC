package com.example.lenovo.osc.StaffFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.osc.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Lenovo on 5/12/2015.
 */
public class NewStockFragment extends Fragment implements View.OnClickListener{

    private static final int RESULT_LOAD_IMAGE = 1;
    protected String[] spinnerCategory;
    protected ImageView imageToUpload;
    protected Spinner sCategory;
    protected TextView tvAddStockID;
    protected EditText tfAddStockName;
    protected EditText tfAddStockCost;
    protected EditText tfAddStockPrice;
    protected EditText tfAddStockQuantity;
    protected EditText tfAddStockDescription;
    protected EditText tfAddStockLocation;
    protected EditText tfAddStockSupplier;
    protected Button bAddStock;

    private String id;
    private String name;
    private String category;
    private String cost;
    private String price;
    private String quantity;
    private String description;
    private String location;
    private String supplierID;
    private String supplierName;
    private String supplierCompany;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_new_stock, container, false);

        this.spinnerCategory = new String[] {
                "Category", "Phone", "Tablet", "Laptop", "Mouse", "Keyboard", "Headphone", "Speaker",
                "Console", "Processor", "Other"
        };
        sCategory = (Spinner) view.findViewById(R.id.sCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerCategory);
        sCategory.setAdapter(adapter);

        imageToUpload = (ImageView) view.findViewById(R.id.ivAddStockImage);
        imageToUpload.setOnClickListener(this);
        imageToUpload = null;
        tvAddStockID = (TextView) view.findViewById(R.id.tvAddStockID);
        tfAddStockName = (EditText) view.findViewById(R.id.tfAddStockName);
        tfAddStockCost = (EditText) view.findViewById(R.id.tfAddStockCost);
        tfAddStockPrice = (EditText) view.findViewById(R.id.tfAddStockPrice);
        tfAddStockQuantity = (EditText) view.findViewById(R.id.tfAddStockQuantity);
        tfAddStockDescription = (EditText) view.findViewById(R.id.tfAddStockDescription);
        tfAddStockLocation = (EditText) view.findViewById(R.id.tfAddStockLocation);
        tfAddStockSupplier = (EditText) view.findViewById(R.id.tfAddStockSupplier);
        tfAddStockSupplier.setOnClickListener(this);
        bAddStock = (Button) view.findViewById(R.id.bAddStock);
        bAddStock.setOnClickListener(this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentreStock");
        query.orderByDescending("createdAt");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                String tempID = object.getString("CentreStockID");
                String newID = "CS" + (Integer.parseInt(tempID.substring(2,tempID.length()))+1);
                tvAddStockID.setText(newID);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bAddStock:
                addNewStock();
                break;
            case R.id.ivAddStockImage:
                uploadImage();
                break;
            case R.id.tfAddStockSupplier:
                selectSupplier();
                break;
        }
    }

    /**
     * Navigate to gallery to select image.
     */
    public void uploadImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    /**
     * Upload an image from gallery to image view
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
        }
    }

    /**
     * Method to register a new stock.
     */
    public void addNewStock(){
        id = tvAddStockID.getText().toString();
        name = tfAddStockName.getText().toString();
        cost = tfAddStockCost.getText().toString();
        price = tfAddStockPrice.getText().toString();
        quantity = tfAddStockQuantity.getText().toString();
        description = tfAddStockDescription.getText().toString();
        location = tfAddStockLocation.getText().toString();

        category = sCategory.getSelectedItem().toString();

        if (imageToUpload == null) {
            Toast.makeText(getActivity(), "Please select a stock image.", Toast.LENGTH_SHORT).show();
        } else if (category.equalsIgnoreCase("Category")) {
            Toast.makeText(getActivity(), "Please select a category", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            tfAddStockName.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(cost)) {
            tfAddStockCost.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(price)) {
            tfAddStockPrice.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(quantity)) {
            tfAddStockQuantity.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(description)) {
            tfAddStockDescription.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(location)) {
            tfAddStockLocation.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(tfAddStockSupplier.getText().toString())) {
            tfAddStockSupplier.setError("This field cannot be empty.");
        } else {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Supplier");
            query.getInBackground(supplierID, new GetCallback<ParseObject>(){
                @Override
                public void done(ParseObject object, ParseException e) {

                    Bitmap bitmap = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    ParseFile file = new ParseFile(name + ".png", image);
                    file.saveInBackground();

                    ParseObject stock = new ParseObject("CentreStock");
                    stock.put("CentreStockID", id);
                    stock.put("Name", name);
                    stock.put("Image", file);
                    stock.put("Category", category);
                    stock.put("Cost", Double.parseDouble(cost));
                    stock.put("Price", Double.parseDouble(price));
                    stock.put("Quantity", Integer.parseInt(quantity));
                    stock.put("Description", description);
                    stock.put("Location", location);
                    stock.put("SupplierObjectID", object);

                    //Grant permission
                    ParseACL acl = new ParseACL();
                    acl.setPublicReadAccess(true);
                    acl.setPublicWriteAccess(true);
                    stock.setACL(acl);

                    stock.saveInBackground();

                    Toast.makeText(getActivity(), "Stock Registered.", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
                                    supplierID = supplierList.get(which).getObjectId();
                                    supplierName = supplierList.get(which).getString("Name");
                                    tfAddStockSupplier.setText(supplierName);
                                }
                            });
                    builderSingle.create();
                    builderSingle.show();
                }
            }
        });
    }
}
