package com.example.lenovo.osc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class UserProfileActivity extends ActionBarActivity {

    protected TextView tvUserID;
    protected TextView tvStatus;
    protected EditText tfName;
    protected EditText tfIC;
    protected EditText tfTel;
    protected EditText tfEmail;
    protected EditText tfAddress;
    protected EditText tfCompany;
    protected Button bEditUser;
    protected Button bRemoveUser;
    protected Button bConfirmEdit;
    protected Button bCancelEdit;

    private String objectID;
    private String id;
    private String status;
    private String name;
    private String ic;
    private String tel;
    private String email;
    private String address;
    private String company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        bEditUser = (Button)findViewById(R.id.bProfileEdit);
        bRemoveUser = (Button)findViewById(R.id.bProfileRemove);
        bConfirmEdit = (Button)findViewById(R.id.bProfileConfirm);
        bCancelEdit = (Button)findViewById(R.id.bProfileCancel);

        Intent i = getIntent();

        objectID = i.getStringExtra("objectID");
        id = i.getStringExtra("userID");
        status = i.getStringExtra("status");
        name = i.getStringExtra("name");
        ic = i.getStringExtra("ic");
        tel = i.getStringExtra("tel");
        email = i.getStringExtra("email");
        address = i.getStringExtra("address");
        company = i.getStringExtra("company");

        tvUserID = (TextView)findViewById(R.id.tvProfileUserID);
        tvStatus = (TextView)findViewById(R.id.tvProfileStatus);
        tfName = (EditText)findViewById(R.id.tfProfileName);
        tfIC = (EditText)findViewById(R.id.tfProfileIC);
        tfTel = (EditText)findViewById(R.id.tfProfileTel);
        tfEmail = (EditText)findViewById(R.id.tfProfileEmail);
        tfAddress = (EditText)findViewById(R.id.tfProfileAddress);
        tfCompany = (EditText)findViewById(R.id.tfProfileCompany);

        tvUserID.setText(id);
        tvStatus.setText(status);
        tfName.setText(name);
        tfIC.setText(ic);
        tfTel.setText(tel);
        tfEmail.setText(email);
        tfAddress.setText(address);

        if (company != null){
            tfCompany.setVisibility(View.VISIBLE);
            tfCompany.setText(company);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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
     * Enable user data to update.
     * @param view
     */
    public void editUser(View view){
        tfName.setFocusableInTouchMode(true);
        tfTel.setFocusableInTouchMode(true);
        tfEmail.setFocusableInTouchMode(true);
        tfAddress.setFocusableInTouchMode(true);

        if (company !=null)
            tfCompany.setFocusableInTouchMode(true);

        bEditUser.setVisibility(View.INVISIBLE);
        bRemoveUser.setVisibility(View.INVISIBLE);
        bConfirmEdit.setVisibility(View.VISIBLE);
        bCancelEdit.setVisibility(View.VISIBLE);
    }

    /**
     * Deactive a user.
     * @param view
     */
    public void removeUser(View view){

        if (status.equalsIgnoreCase("Admin")){
            Toast.makeText(getApplicationContext(), "Admin cannot be remove.", Toast.LENGTH_SHORT).show();
        } else if (status.equalsIgnoreCase("Inactive")){
            Toast.makeText(getApplicationContext(),"User has been removed.", Toast.LENGTH_SHORT).show();
        } else {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Do your Yes progress
                            ParseQuery<ParseObject> query = ParseQuery.getQuery(determineUser());
                            query.getInBackground(objectID, new GetCallback<ParseObject>() {
                                public void done(ParseObject user, ParseException e) {
                                    if (e == null) {
                                        user.put("Status", "Inactive");
                                        user.saveInBackground();

                                        tvStatus.setText("Inactive");
                                        Toast.makeText(getApplicationContext(),"User removed.", Toast.LENGTH_SHORT).show();
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
            ab.setMessage("Are you sure to remove?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }

    /**
     * Confirm the change.
     * @param view
     */
    public void confirm(View view){

        final String updateName;
        final String updateTel;
        final String updateEmail;
        final String updateAddress;
        final String updateCompany;
        final String error = "This field cannot be empty.";

        updateName = tfName.getText().toString();
        updateTel = tfTel.getText().toString();
        updateEmail = tfEmail.getText().toString();
        updateAddress = tfAddress.getText().toString();
        updateCompany = tfCompany.getText().toString();

        if (TextUtils.isEmpty(updateName)){
            tfName.setError(error);
        } else if (TextUtils.isEmpty(updateTel)){
            tfTel.setError(error);
        } else if (TextUtils.isEmpty(updateEmail)){
            tfEmail.setError(error);
        } else if (TextUtils.isEmpty(updateAddress)){
            tfAddress.setError(error);
        } else if (company != null && TextUtils.isEmpty(updateCompany)){
            tfCompany.setError(error);
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(determineUser());
            query.getInBackground(objectID, new GetCallback<ParseObject>() {
                public void done(ParseObject user, ParseException e) {
                    if (e == null) {
                        user.put("Name", updateName);
                        user.put("Tel", updateTel);
                        user.put("Email", updateEmail);
                        user.put("Address", updateAddress);

                        if (company != null) {
                                user.put("Company", updateCompany);
                                tfCompany.setFocusableInTouchMode(false);
                        }
                        user.saveInBackground();

                        tfName.setFocusableInTouchMode(false);
                        tfTel.setFocusableInTouchMode(false);
                        tfEmail.setFocusableInTouchMode(false);
                        tfAddress.setFocusableInTouchMode(false);

                        bEditUser.setVisibility(View.VISIBLE);
                        bRemoveUser.setVisibility(View.VISIBLE);
                        bConfirmEdit.setVisibility(View.INVISIBLE);
                        bCancelEdit.setVisibility(View.INVISIBLE);

                        Toast.makeText(getApplicationContext(), "User updated.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserProfileActivity.this, UpdateUserFragment.class));
                    }
                }
            });
        }
    }

    /**
     * Cancel the update
     * @param view
     */
    public void cancel(View view){
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    /**
     * Determine current user type.
     * @return
     */
    public String determineUser(){

        String userType ;

        if (id.charAt(0) == 'S'){
            userType = "Staff";
        } else if (id.charAt(0) == 'U'){
            userType = "Supplier";
        } else if (id.charAt(0) == 'T'){
            userType = "Stockist";
        } else {
            Toast.makeText(getApplicationContext(),"User does not exist.", Toast.LENGTH_SHORT).show();
            userType = null;
        }

        return userType;
    }
}
