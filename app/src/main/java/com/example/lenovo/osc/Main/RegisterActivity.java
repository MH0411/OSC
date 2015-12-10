package com.example.lenovo.osc.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.osc.R;
import com.parse.ParseACL;
import com.parse.ParseObject;


public class RegisterActivity extends ActionBarActivity {

    protected EditText tfStockistID;
    protected EditText tfName;
    protected EditText tfPassword;
    protected EditText tfIC;
    protected EditText tfTel;
    protected EditText tfEmail;
    protected EditText tfAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

        tfStockistID = (EditText) findViewById(R.id.tfRegStockistID);
        tfName = (EditText) findViewById(R.id.tfRegName);
        tfPassword = (EditText) findViewById(R.id.tfRegPassword);
        tfIC = (EditText) findViewById(R.id.tfRegIC);
        tfTel = (EditText) findViewById(R.id.tfRegTel);
        tfEmail = (EditText) findViewById(R.id.tfRegEmail);
        tfAddress = (EditText) findViewById(R.id.tfRegAddress);

        String stockistID = tfStockistID.getText().toString();
        String name = tfName.getText().toString();
        String password = tfPassword.getText().toString();
        String ic = tfIC.getText().toString();
        String tel = tfTel.getText().toString();
        String email = tfEmail.getText().toString();
        String address = tfAddress.getText().toString();

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
            Toast.makeText(getApplicationContext(), "Register Successful.", Toast.LENGTH_SHORT).show();
        }
    }
}
