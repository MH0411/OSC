package com.example.lenovo.osc;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;


public class LoginActivity extends ActionBarActivity {

    protected EditText tfUserID;
    protected EditText tfPassword;
    protected Button bLogin;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

                Toast.makeText(getApplicationContext(), "Please fill all empty fields", Toast.LENGTH_SHORT).show();
                count++;

            } else {

                //check username space
                for (int index = 0; index < userID.length(); index++) {
                    if (userID.charAt(index) == ' ') {

                        Toast.makeText(getApplicationContext(), "UserID cannot contains spaces", Toast.LENGTH_SHORT).show();
                        count++;
                        break;

                    } else if (index == userID.length() - 1) {

                        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                        dialog.setMessage("Loging in");
                        dialog.setCancelable(false);
                        dialog.setInverseBackgroundForced(false);
                        dialog.show();

                        // doing login function in backend
                        ParseUser.logInInBackground(userID, password, new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {

                                if (user != null) {
                                    // Hooray! The user is logged in.
                                    // redirect to main page
                                    dialog.dismiss();
                                    //Associate the device with a user
                                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                    installation.put("userID", ParseUser.getCurrentUser().getObjectId());
                                    installation.saveInBackground();
                                    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Login failed. Look at the ParseException to see what happened.
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                    count++;
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}
