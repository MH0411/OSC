//package com.example.lenovo.osc.Main;
//
//import com.parse.FindCallback;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//
//import java.util.List;
//
///**
// * Created by Lenovo on 9/12/2015.
// */
//public class Logout {
//
//    private String userID;
//    private String userType;
//
//    public void logout(){
//        userType();
//        ParseObject object = ParseObject.createWithoutData(userType, LoginActivity.currentUser.getObjectID());
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("LoginState");
//        query.include(userType);
//        query.whereEqualTo(userType, object);
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if (e == null){
//                    objects.get(0).deleteInBackground();
//                }
//            }
//        });
//    }
//
//    public void userType(){
//        userID = LoginActivity.currentUser.getUserID();
//        if (userID.charAt(0) == 'S')
//            userType = "Staff";
//        else if (userID.charAt(0) == 'U')
//            userType = "Supplier";
//        else if( userID.charAt(0) == 'T')
//            userType = "Stockist";
//    }
//}
