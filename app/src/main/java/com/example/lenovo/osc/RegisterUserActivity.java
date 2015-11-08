package com.example.lenovo.osc;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;


public class RegisterUserActivity extends ActionBarActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_user, menu);
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

    public void staff(View view){
        tfCompany = (EditText)findViewById(R.id.tfAdminCompany);
        tfUserID = (EditText)findViewById(R.id.tfAdminUserID);
        bRegisterStaff = (Button)findViewById(R.id.bRegisterStaff);
        bRegisterSupplier = (Button)findViewById(R.id.bRegisterSupplier);

        bRegisterStaff.setVisibility(View.VISIBLE);
        bRegisterSupplier.setVisibility(View.INVISIBLE);
        tfCompany.setVisibility(View.INVISIBLE);
        tfUserID.setText("S");
    }

    public void supplier(View view){
        tfCompany = (EditText)findViewById(R.id.tfAdminCompany);
        tfUserID = (EditText)findViewById(R.id.tfAdminUserID);
        bRegisterSupplier = (Button)findViewById(R.id.bRegisterSupplier);
        bRegisterStaff = (Button)findViewById(R.id.bRegisterStaff);

        bRegisterSupplier.setVisibility(View.VISIBLE);
        bRegisterStaff.setVisibility(View.INVISIBLE);
        tfCompany.setVisibility(View.VISIBLE);
        tfUserID.setText("U");
    }

    public void registerSupplier(View view){
        tfUserID = (EditText)findViewById(R.id.tfAdminUserID);
        tfName = (EditText)findViewById(R.id.tfAdminName);
        tfPassword = (EditText)findViewById(R.id.tfAdminPassword);
        tfIC = (EditText)findViewById(R.id.tfAdminIC);
        tfTel = (EditText)findViewById(R.id.tfAdminTel);
        tfEmail = (EditText)findViewById(R.id.tfAdminEmail);
        tfAddress = (EditText)findViewById(R.id.tfAdminAddress);
        tfCompany = (EditText)findViewById(R.id.tfAdminCompany);

        String userID = tfUserID.getText().toString();
        String name = tfName.getText().toString();
        String password = tfPassword.getText().toString();
        String ic = tfIC.getText().toString();
        String tel = tfTel.getText().toString();
        String email = tfEmail.getText().toString();
        String address = tfAddress.getText().toString();
        String company = tfCompany.getText().toString();

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
        } else {

            ParseObject regUser = new ParseObject("Supplier");
            regUser.put("SupplierID", userID);
            regUser.put("Name", name);
            regUser.put("Password", password);
            regUser.put("IC", ic);
            regUser.put("Tel", tel);
            regUser.put("Email", email);
            regUser.put("Address", address);
            regUser.put("Company", company);
            regUser.put("Status", "Active");
            regUser.saveInBackground();

            Toast.makeText(getApplicationContext(), "Register Supplier Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void registerStaff(View view) {
        tfUserID = (EditText) findViewById(R.id.tfAdminUserID);
        tfName = (EditText) findViewById(R.id.tfAdminName);
        tfPassword = (EditText) findViewById(R.id.tfAdminPassword);
        tfIC = (EditText) findViewById(R.id.tfAdminIC);
        tfTel = (EditText) findViewById(R.id.tfAdminTel);
        tfEmail = (EditText) findViewById(R.id.tfAdminEmail);
        tfAddress = (EditText) findViewById(R.id.tfAdminAddress);

        String userID = tfUserID.getText().toString();
        String name = tfName.getText().toString();
        String password = tfPassword.getText().toString();
        String ic = tfIC.getText().toString();
        String tel = tfTel.getText().toString();
        String email = tfEmail.getText().toString();
        String address = tfAddress.getText().toString();

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
        } else {

            ParseObject regUser = new ParseObject("Staff");
            regUser.put("StaffID", userID);
            regUser.put("Name", name);
            regUser.put("Password", password);
            regUser.put("IC", ic);
            regUser.put("Tel", tel);
            regUser.put("Email", email);
            regUser.put("Address", address);
            regUser.put("Status", "Active");
            regUser.saveInBackground();

            Toast.makeText(getApplicationContext(), "Register Staff Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
