package com.example.lenovo.osc.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lenovo.osc.R;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;


public class RegisterActivity extends ActionBarActivity {

    protected EditText tfStockistID;
    protected EditText tfName;
    protected EditText tfPassword;
    protected EditText tfIC;
    protected EditText tfTel;
    protected EditText tfEmail;
    protected EditText tfAddress;
    protected ImageView imageToUpload;

    private static final int RESULT_LOAD_IMAGE = 1;
    private String stockistID;
    private String name;
    private String password;
    private String ic;
    private String tel;
    private String email;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tfStockistID = (EditText) findViewById(R.id.tfRegStockistID);
        tfName = (EditText) findViewById(R.id.tfRegName);
        tfPassword = (EditText) findViewById(R.id.tfRegPassword);
        tfIC = (EditText) findViewById(R.id.tfRegIC);
        tfTel = (EditText) findViewById(R.id.tfRegTel);
        tfEmail = (EditText) findViewById(R.id.tfRegEmail);
        tfAddress = (EditText) findViewById(R.id.tfRegAddress);
        imageToUpload = (ImageView) findViewById(R.id.ivRegisterStockist);

        //Set stockist ID
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Stockist");
        query.orderByDescending("createdAt");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                String tempID = object.getString("StockistID");
                Toast.makeText(getApplication(), tempID, Toast.LENGTH_SHORT).show();
                String newID = "T" + (Integer.parseInt(tempID.substring(1, tempID.length())) + 1);
                tfStockistID.setText(newID);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    public void register(View view) {

        stockistID = tfStockistID.getText().toString();
        name = tfName.getText().toString();
        password = tfPassword.getText().toString();
        ic = tfIC.getText().toString();
        tel = tfTel.getText().toString();
        email = tfEmail.getText().toString();
        address = tfAddress.getText().toString();

        if (TextUtils.isEmpty(stockistID)) {
            tfStockistID.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(name)) {
            tfName.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(password)) {
            tfPassword.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(ic)) {
            tfIC.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(tel)) {
            tfTel.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(email)) {
            tfEmail.setError("This field cannot be empty.");
        } else if (TextUtils.isEmpty(address)) {
            tfAddress.setError("This field cannot be empty.");
        } else {

            ParseObject regUser = new ParseObject("Stockist");
            regUser.put("StockistID", stockistID);
            regUser.put("Name", name);
            regUser.put("ProfilePic", convertImage());
            regUser.put("Password", password);
            regUser.put("IC", ic);
            regUser.put("Tel", tel);
            regUser.put("Email", email);
            regUser.put("Address", address);
            regUser.put("Status", "Active");

            //Grant permission
            ParseACL acl = new ParseACL();
            acl.setPublicReadAccess(true);
            acl.setPublicWriteAccess(true);
            regUser.setACL(acl);
            //Save data
            regUser.saveInBackground();

            startActivity(new Intent(this, LoginActivity.class));
            finish();
            Toast.makeText(getApplicationContext(), "Register Successful.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Convert selected image to bytes
     * @return
     */
    public ParseFile convertImage(){
        Bitmap bitmap = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        ParseFile file = new ParseFile(name + ".png", image);
        file.saveInBackground();
        return file;
    }

    /**
     * Go to gallery select image
     * @param v
     */
    public void uploadImage(View v){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    /**
     * Set image from gallery
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            imageToUpload.setImageURI(data.getData());
        }
    }
}
