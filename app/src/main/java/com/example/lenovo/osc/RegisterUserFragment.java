package com.example.lenovo.osc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;

/**
 * Created by Lenovo on 25/11/2015.
 */
public class RegisterUserFragment extends Fragment implements View.OnClickListener{

    protected EditText tfUserID;
    protected EditText tfName;
    protected EditText tfPassword;
    protected EditText tfIC;
    protected EditText tfTel;
    protected EditText tfEmail;
    protected EditText tfAddress;
    protected EditText tfCompany;
    protected Button bRegisterStaff;
    protected Button bRegisterSupplier;
    protected RadioButton rbStaff;
    protected RadioButton rbSupplier;
    protected ImageView imageToUpload = null;

    private static final int RESULT_LOAD_IMAGE = 1;
    private String userID;
    private String name;
    private String password;
    private String ic;
    private String tel;
    private String email;
    private String address;
    private String company;

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_register_user, container, false);

        tfUserID = (EditText) myView.findViewById(R.id.tfAdminUserID);
        tfName = (EditText) myView.findViewById(R.id.tfAdminName);
        tfPassword = (EditText) myView.findViewById(R.id.tfAdminPassword);
        tfIC = (EditText) myView.findViewById(R.id.tfAdminIC);
        tfTel = (EditText) myView.findViewById(R.id.tfAdminTel);
        tfEmail = (EditText) myView.findViewById(R.id.tfAdminEmail);
        tfAddress = (EditText) myView.findViewById(R.id.tfAdminAddress);
        tfCompany = (EditText) myView.findViewById(R.id.tfAdminCompany);

        imageToUpload = (ImageView) myView.findViewById(R.id.ivUserProfileImage);
        imageToUpload.setOnClickListener(this);

        bRegisterSupplier = (Button) myView.findViewById(R.id.bRegisterSupplier);
        bRegisterStaff = (Button) myView.findViewById(R.id.bRegisterStaff);
        bRegisterSupplier.setOnClickListener(this);
        bRegisterStaff.setOnClickListener(this);

        rbStaff = (RadioButton) myView.findViewById(R.id.rbStaff);
        rbSupplier = (RadioButton) myView.findViewById(R.id.rbSupplier);
        rbStaff.setOnClickListener(this);
        rbSupplier.setOnClickListener(this);

        //Set staff ID
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Staff");
        query.orderByDescending("createdAt");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                String tempID = object.getString("StaffID");
                String newID = "S" + (Integer.parseInt(tempID.substring(1, tempID.length())) + 1);
                tfUserID.setText(newID);
            }
        });

        return myView;
    }

    /**
     * Method to handle action taken when component is trigger.
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bRegisterStaff:
                registerStaff(v);
                break;

            case R.id.bRegisterSupplier:
                registerSupplier(v);
                break;

            case R.id.rbStaff:
                bRegisterStaff.setVisibility(View.VISIBLE);
                bRegisterSupplier.setVisibility(View.INVISIBLE);
                tfCompany.setVisibility(View.INVISIBLE);

                //Set staff ID
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Staff");
                query.orderByDescending("createdAt");
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        String tempID = object.getString("StaffID");
                        String newID = "S" + (Integer.parseInt(tempID.substring(1, tempID.length())) + 1);
                        tfUserID.setText(newID);
                    }
                });
                break;

            case R.id.rbSupplier:
                bRegisterSupplier.setVisibility(View.VISIBLE);
                bRegisterStaff.setVisibility(View.INVISIBLE);
                tfCompany.setVisibility(View.VISIBLE);

                //Set Supplier ID
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Supplier");
                query1.orderByDescending("createdAt");
                query1.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        String tempID = object.getString("SupplierID");
                        String newID = "U" + (Integer.parseInt(tempID.substring(1, tempID.length())) + 1);
                        tfUserID.setText(newID);
                    }
                });
                break;

            case R.id.ivUserProfileImage:
                //Go to gallery select image
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
        }
    }

    /**
     * Register supplier
     * @param view
     */
    public void registerSupplier(View view){

        userID = tfUserID.getText().toString();
        name = tfName.getText().toString();
        password = tfPassword.getText().toString();
        ic = tfIC.getText().toString();
        tel = tfTel.getText().toString();
        email = tfEmail.getText().toString();
        address = tfAddress.getText().toString();
        company = tfCompany.getText().toString();

        //Register supplier
        if (TextUtils.isEmpty(userID)) {
            tfUserID.setError("This field cannot be empty.");
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
        } else if (TextUtils.isEmpty(company)) {
            tfCompany.setError("This field cannot be empty.");
        } else if (imageToUpload == null) {
            Toast.makeText(getActivity(),"Please select a profile picture.",Toast.LENGTH_SHORT).show();
        } else {

            ParseObject regUser = new ParseObject("Supplier");
            regUser.put("SupplierID", userID);
            regUser.put("ProfilePic", convertImage());
            regUser.put("Name", name);
            regUser.put("Password", password);
            regUser.put("IC", ic);
            regUser.put("Tel", tel);
            regUser.put("Email", email);
            regUser.put("Address", address);
            regUser.put("Company", company);
            regUser.put("Status", "Active");

            //Grant permission
            ParseACL acl = new ParseACL();
            acl.setPublicReadAccess(true);
            acl.setPublicWriteAccess(true);
            regUser.setACL(acl);

            //Save data
            regUser.saveInBackground();

            Toast.makeText(getActivity(), "Register Supplier Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Register staff
     * @param view
     */
    public void registerStaff(View view) {

        userID = tfUserID.getText().toString();
        name = tfName.getText().toString();
        password = tfPassword.getText().toString();
        ic = tfIC.getText().toString();
        tel = tfTel.getText().toString();
        email = tfEmail.getText().toString();
        address = tfAddress.getText().toString();

        //Register staff
        if (TextUtils.isEmpty(userID)) {
            tfUserID.setError("This field cannot be empty.");
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
        } else if (imageToUpload == null) {
            Toast.makeText(getActivity(),"Please select a profile picture.",Toast.LENGTH_SHORT).show();
        } else {

            ParseObject regUser = new ParseObject("Staff");
            regUser.put("StaffID", userID);
            regUser.put("ProfilePic", convertImage());
            regUser.put("Name", name);
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

            Toast.makeText(getActivity(), "Register Staff Successfully", Toast.LENGTH_SHORT).show();
        }
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
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null){
            imageToUpload.setImageURI(data.getData());
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
}
