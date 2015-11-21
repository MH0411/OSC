package com.example.lenovo.osc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;


public class NewStockActivity extends ActionBarActivity {

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

    private String id;
    private String name;
    private String category;
    private String cost;
    private String price;
    private String quantity;
    private String description;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_stock);

        this.spinnerCategory = new String[] {
                "Category", "Phone", "Tablet", "Laptop", "Mouse", "Keyboard", "Headphone", "Speaker",
                "Console", "Processor", "Other"
        };
        sCategory = (Spinner) findViewById(R.id.sCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerCategory);
        sCategory.setAdapter(adapter);

        imageToUpload = null;
        tvAddStockID = (TextView)findViewById(R.id.tvAddStockID);
        tfAddStockName = (EditText)findViewById(R.id.tfAddStockName);
        tfAddStockCost = (EditText)findViewById(R.id.tfAddStockCost);
        tfAddStockPrice = (EditText)findViewById(R.id.tfAddStockPrice);
        tfAddStockQuantity = (EditText)findViewById(R.id.tfAddStockQuantity);
        tfAddStockDescription = (EditText)findViewById(R.id.tfAddStockDescription);
        tfAddStockLocation = (EditText)findViewById(R.id.tfAddStockLocation);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_stock, menu);
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

    public void uploadImage(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageToUpload = (ImageView)findViewById(R.id.ivAddStockImage);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
        }
    }

    public void addNewStock(View view) {

        id = tvAddStockID.getText().toString();
        name = tfAddStockName.getText().toString();
        cost = tfAddStockCost.getText().toString();
        price = tfAddStockPrice.getText().toString();
        quantity = tfAddStockQuantity.getText().toString();
        description = tfAddStockDescription.getText().toString();
        location = tfAddStockLocation.getText().toString();
        category = sCategory.getSelectedItem().toString();

        if (imageToUpload == null) {
            Toast.makeText(getApplicationContext(),"Please select a stock image.",Toast.LENGTH_SHORT).show();
        } else if (category.equalsIgnoreCase("Category")){
            Toast.makeText(getApplicationContext(), "Please select a category", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)){
            tfAddStockName.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(cost)){
            tfAddStockCost.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(price)){
            tfAddStockPrice.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(quantity)){
            tfAddStockQuantity.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(description)){
            tfAddStockDescription.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(location)){
            tfAddStockLocation.setError("This field cannot be empty.");
        } else {

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
            stock.put("Cost", cost);
            stock.put("Price", Double.parseDouble(price));
            stock.put("Quantity", Integer.parseInt(quantity));
            stock.put("Description", description);
            stock.put("Location", location);
            stock.saveInBackground();

            Toast.makeText(getApplicationContext(), "Stock Registered.", Toast.LENGTH_SHORT).show();
        }
    }
}
