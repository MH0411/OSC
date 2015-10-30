package com.example.lenovo.osc;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by Lenovo on 28/10/2015.
 */
public class OSCApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

		/*
		 * Add Parse initialization code here
		 */

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "4ExqwNOAj0zreRh9GoQY2NmitnT0cUDDw35S8SIe",
                "Z5TvgtWwJBXtbJiXLJ3pF5bVvjenN0B9KbhP61xn");

        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}
