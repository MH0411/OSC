package com.example.lenovo.osc.Main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.osc.Menu.AdminMenuActivity;
import com.example.lenovo.osc.Menu.StaffMenuActivity;
import com.example.lenovo.osc.Menu.StockistMenuActivity;
import com.example.lenovo.osc.R;
import com.example.lenovo.osc.SupplierFunction.SupplierOrderListActivity;
import com.example.lenovo.osc.Users.User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LoginActivity extends ActionBarActivity {

    protected EditText tfUserID;
    protected EditText tfPassword;
    protected Button bLogin;

    public static User currentUser;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Check if current logged out
        if (prefs.getString("loginState", "").equalsIgnoreCase("true")) {

            //Navigate to activity
            if (prefs.getString("userId", "").charAt(0) == 'S'){

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Staff");
                query.whereEqualTo("StaffID", prefs.getString("userId", ""));
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        currentUser = new User(
                                object.getObjectId(),
                                object.getString("StaffID"),
                                object.getString("Password"),
                                object.getString("Name"),
                                object.getString("IC"),
                                object.getString("Tel"),
                                object.getString("Email"),
                                object.getString("Address"),
                                object.getString("Status")
                        );
                        if (currentUser.getStatus().equalsIgnoreCase("Admin"))
                            startActivity(new Intent(LoginActivity.this, AdminMenuActivity.class));
                        else
                            startActivity(new Intent(LoginActivity.this, StaffMenuActivity.class));

                        finish();
                    };
                });

            } else if (prefs.getString("userId", "").charAt(0) == 'T'){

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Stockist");
                query.whereEqualTo("StockistID", prefs.getString("userId", ""));
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        currentUser = new User(
                                object.getObjectId(),
                                object.getString("StaffID"),
                                object.getString("Password"),
                                object.getString("Name"),
                                object.getString("IC"),
                                object.getString("Tel"),
                                object.getString("Email"),
                                object.getString("Address"),
                                object.getString("Status")
                        );

                        startActivity(new Intent(LoginActivity.this, StockistMenuActivity.class));
                        finish();
                    };
                });

            } else if (prefs.getString("userId", "").charAt(0) == 'U'){

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Supplier");
                query.whereEqualTo("SupplierID", prefs.getString("userId", ""));
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        currentUser = new User(
                                object.getObjectId(),
                                object.getString("SupplierID"),
                                object.getString("Password"),
                                object.getString("Name"),
                                object.getString("IC"),
                                object.getString("Tel"),
                                object.getString("Email"),
                                object.getString("Address"),
                                object.getString("Company"),
                                object.getString("Status")
                        );

                        startActivity(new Intent(LoginActivity.this, SupplierOrderListActivity.class));
                        finish();
                    };
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        return true;
    }

    /**
     * This method used to login.
     * @param view
     */
    public void login(View view){

        tfUserID = (EditText) findViewById(R.id.tfLoginUserID);
        tfPassword = (EditText) findViewById(R.id.tfLoginPassword);
        bLogin = (Button) findViewById(R.id.bLogin);

        // get username input and password input
        final String userID = tfUserID.getText().toString();
        String password = tfPassword.getText().toString();

        if (count == 2) {

            final KeyListener usernameListener;
            final KeyListener passwordListener;
            usernameListener = tfUserID.getKeyListener();
            passwordListener = tfPassword.getKeyListener();

            //if try login 3 time in a row.
            Toast.makeText(getApplicationContext(), "You have attempt 3 consecutive login." +
                    "\nPlease try again later", Toast.LENGTH_SHORT).show();
            new CountDownTimer(30000, 1000) {

                TextView timer = (TextView) findViewById(R.id.tvTimer);

                public void onTick(long millisUntilFinished) {

                    tfUserID.setKeyListener(null);
                    tfPassword.setKeyListener(null);
                    timer.setVisibility(View.VISIBLE);
                    bLogin.setVisibility(View.INVISIBLE);
                    timer.setText("Seconds Remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {

                    count = 0;
                    bLogin.setVisibility(View.VISIBLE);
                    timer.setVisibility(View.INVISIBLE);
                    tfUserID.setKeyListener(usernameListener);
                    tfPassword.setKeyListener(passwordListener);
                }
            }.start();

        } else {
            //check empty fields
            if (userID.matches("") || password.matches("")) {

                Toast.makeText(getApplicationContext(), "Please fill all empty fields",
                        Toast.LENGTH_SHORT).show();
                count++;

            } else {

                //check username space
                for (int index = 0; index < userID.length(); index++) {
                    if (userID.charAt(index) == ' ') {

                        Toast.makeText(getApplicationContext(), "UserID cannot contains spaces",
                                Toast.LENGTH_SHORT).show();
                        count++;
                        break;

                    } else if (index == userID.length() - 1) {

                        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                        dialog.setMessage("Loging in");
                        dialog.setCancelable(false);
                        dialog.setInverseBackgroundForced(false);
                        dialog.show();

                        if(userID.charAt(0) == 'S') {

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Staff");
                            query.whereEqualTo("StaffID", userID);
                            query.whereEqualTo("Password", password);
                            query.getFirstInBackground(new GetCallback<ParseObject>() {

                                @Override
                                public void done(ParseObject object, ParseException e) {

                                    dialog.dismiss();
                                    if (object == null){
                                        Toast.makeText(getApplicationContext(),"Login Failed.",
                                                Toast.LENGTH_SHORT).show();
                                        count++;
                                    } else {
                                        //Associate the device with a user
                                        ParseInstallation installation =
                                                ParseInstallation.getCurrentInstallation();
                                        installation.put("userID", object);
                                        installation.saveInBackground();

                                        //Set user data in user class.
                                        currentUser = new User(
                                                object.getObjectId(),
                                                object.getString("StaffID"),
                                                object.getString("Password"),
                                                object.getString("Name"),
                                                object.getString("IC"),
                                                object.getString("Tel"),
                                                object.getString("Email"),
                                                object.getString("Address"),
                                                object.getString("Status")
                                        );
                                        //Go to admin home page
                                        if (currentUser.getStatus().equalsIgnoreCase("Admin"))
                                            startActivity(new Intent(LoginActivity.this,
                                                    AdminMenuActivity.class));
                                        //Go to staff home page
                                        else
                                            startActivity(new Intent(LoginActivity.this,
                                                    StaffMenuActivity.class));

                                        Toast.makeText(getApplicationContext(),
                                                "Welcome " + currentUser.getName(),
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });

                        } else if (userID.charAt(0) == 'T') {

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Stockist");
                            query.whereEqualTo("StockistID", userID);
                            query.whereEqualTo("Password", password);
                            query.getFirstInBackground(new GetCallback<ParseObject>() {

                                @Override
                                public void done(ParseObject object, ParseException e) {

                                    dialog.dismiss();
                                    if (object == null){
                                        Toast.makeText(getApplicationContext(),"Login Failed.",
                                                Toast.LENGTH_SHORT).show();
                                        count++;
                                    } else {
                                        //Associate the device with a user
                                        ParseInstallation installation =
                                                ParseInstallation.getCurrentInstallation();
                                        installation.put("userID", object);
                                        installation.saveInBackground();

                                        //Set user data in user class.
                                        currentUser = new User(
                                                object.getObjectId(),
                                                object.getString("StockistID"),
                                                object.getString("Password"),
                                                object.getString("Name"),
                                                object.getString("IC"),
                                                object.getString("Tel"),
                                                object.getString("Email"),
                                                object.getString("Address"),
                                                object.getString("Status")
                                        );
                                        Toast.makeText(getApplicationContext(),
                                                "Welcome " + currentUser.getName(),
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this,
                                                StockistMenuActivity.class));
                                        finish();
                                    }
                                }
                            });

                        } else if (userID.charAt(0) == 'U') {

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Supplier");
                            query.whereEqualTo("SupplierID", userID);
                            query.whereEqualTo("Password", password);
                            query.getFirstInBackground(new GetCallback<ParseObject>() {

                                @Override
                                public void done(ParseObject object, ParseException e) {

                                    dialog.dismiss();
                                    if (object == null){
                                        Toast.makeText(getApplicationContext(),"Login Failed.",
                                                Toast.LENGTH_SHORT).show();
                                        count++;
                                    } else {
                                        //Associate the device with a user
                                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                        installation.put("userID", object);
                                        installation.saveInBackground();

                                        //Set user data in user class.
                                        currentUser = new User(
                                                object.getObjectId(),
                                                object.getString("SupplierID"),
                                                object.getString("Password"),
                                                object.getString("Name"),
                                                object.getString("IC"),
                                                object.getString("Tel"),
                                                object.getString("Email"),
                                                object.getString("Address"),
                                                object.getString("Company"),
                                                object.getString("Status")
                                        );

                                        Toast.makeText(getApplicationContext(),
                                                "Welcome " + currentUser.getName(),
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this,
                                                SupplierOrderListActivity.class));
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(),"Login Failed.",
                                    Toast.LENGTH_SHORT).show();
                            count++;
                        }
                    }
                }
            }
        }
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
