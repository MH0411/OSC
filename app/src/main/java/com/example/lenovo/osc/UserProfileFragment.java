package com.example.lenovo.osc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.osc.Users.User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

/**
 * Created by Lenovo on 6/12/2015.
 */
public class UserProfileFragment extends Fragment implements View.OnClickListener {

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
    protected ImageView ivProfilePic;

    private User user;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_user_profile, container, false);

        bEditUser = (Button) view.findViewById(R.id.bProfileEdit);
        bEditUser.setOnClickListener(this);
        bRemoveUser = (Button) view.findViewById(R.id.bProfileRemove);
        bRemoveUser.setOnClickListener(this);
        bConfirmEdit = (Button) view.findViewById(R.id.bProfileConfirm);
        bConfirmEdit.setOnClickListener(this);
        bCancelEdit = (Button) view.findViewById(R.id.bProfileCancel);
        bCancelEdit.setOnClickListener(this);

        user = new User(
                getArguments().getString("objectID"),
                getArguments().getString("userID"),
                getArguments().getString("status"),
                getArguments().getString("name"),
                getArguments().getString("ic"),
                getArguments().getString("tel"),
                getArguments().getString("email"),
                getArguments().getString("address"),
                getArguments().getString("company")
        );

        ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);
        tvUserID = (TextView) view.findViewById(R.id.tvProfileUserID);
        tvStatus = (TextView) view.findViewById(R.id.tvProfileStatus);
        tfName = (EditText) view.findViewById(R.id.tfProfileName);
        tfIC = (EditText) view.findViewById(R.id.tfProfileIC);
        tfTel = (EditText) view.findViewById(R.id.tfProfileTel);
        tfEmail = (EditText) view.findViewById(R.id.tfProfileEmail);
        tfAddress = (EditText) view.findViewById(R.id.tfProfileAddress);
        tfCompany = (EditText) view.findViewById(R.id.tfProfileCompany);

        tvUserID.setText(user.getUserID());
        tvStatus.setText(user.getStatus());
        tfName.setText(user.getName());
        tfIC.setText(user.getNoIC());
        tfTel.setText(user.getNoTel());
        tfEmail.setText(user.getEmail());
        tfAddress.setText(user.getAddress());

        if (user.getCompany() != null){
            tfCompany.setVisibility(View.VISIBLE);
            tfCompany.setText(user.getCompany());
        }

        loadImage();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bProfileEdit:
                editUser();
                break;
            case R.id.bProfileRemove:
                removeUser();
                break;
            case R.id.bProfileConfirm:
                confirm();
                break;
            case R.id.bProfileCancel:
                cancel();
                break;
        }
    }

    /**
     * Enable user data to update.
     */
    public void editUser(){
        tfName.setFocusableInTouchMode(true);
        tfTel.setFocusableInTouchMode(true);
        tfEmail.setFocusableInTouchMode(true);
        tfAddress.setFocusableInTouchMode(true);

        if (user.getCompany() !=null)
            tfCompany.setFocusableInTouchMode(true);

        bEditUser.setVisibility(View.INVISIBLE);
        bRemoveUser.setVisibility(View.INVISIBLE);
        bConfirmEdit.setVisibility(View.VISIBLE);
        bCancelEdit.setVisibility(View.VISIBLE);
    }

    /**
     * Deactive a user.
     */
    public void removeUser(){

        if (user.getStatus().equalsIgnoreCase("Admin")){
            Toast.makeText(getActivity(), "Admin cannot be remove.", Toast.LENGTH_SHORT).show();
        } else if (user.getStatus().equalsIgnoreCase("Inactive")){
            Toast.makeText(getActivity(),"User has been removed.", Toast.LENGTH_SHORT).show();
        } else {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Do your Yes progress
                            ParseQuery<ParseObject> query = ParseQuery.getQuery(determineUser());
                            query.getInBackground(user.getObjectID(), new GetCallback<ParseObject>() {
                                public void done(ParseObject user, ParseException e) {
                                    if (e == null) {
                                        user.put("Status", "Inactive");
                                        user.saveInBackground();

                                        tvStatus.setText("Inactive");
                                        Toast.makeText(getActivity(),"User removed.", Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
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
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setMessage("Are you sure to remove?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }

    /**
     * Confirm the change.
     */
    public void confirm(){

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
        } else if (user.getCompany() != null && TextUtils.isEmpty(updateCompany)){
            tfCompany.setError(error);
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(determineUser());
            query.getInBackground(user.getObjectID(), new GetCallback<ParseObject>() {
                public void done(ParseObject user, ParseException e) {
                    if (e == null) {
                        user.put("Name", updateName);
                        user.put("Tel", updateTel);
                        user.put("Email", updateEmail);
                        user.put("Address", updateAddress);

                        if (updateCompany != null) {
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

                        Toast.makeText(getActivity(), "User updated.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), UpdateUserFragment.class));
                    }
                }
            });
        }
    }

    /**
     * Cancel the update
     */
    public void cancel(){
        Intent intent = getActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().finish();
        startActivity(intent);
    }

    /**
     * Determine current user type.
     * @return
     */
    public String determineUser(){

        String userType ;

        if (user.getUserID().charAt(0) == 'S'){
            userType = "Staff";
        } else if (user.getUserID().charAt(0) == 'U'){
            userType = "Supplier";
        } else if (user.getUserID().charAt(0) == 'T'){
            userType = "Stockist";
        } else {
            Toast.makeText(getActivity(),"User does not exist.", Toast.LENGTH_SHORT).show();
            userType = null;
        }

        return userType;
    }

    /**
     * Load user profile picture.
     */
    public void loadImage(){

        //Load the selected staff's profile picture.
        ParseQuery<ParseObject> query = ParseQuery.getQuery(determineUser());
        query.getInBackground(user.getObjectID(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Picasso.with(getActivity().getApplicationContext()).load(object.getParseFile("ProfilePic").getUrl()).noFade().into(ivProfilePic);
                }
            }
        });
    }
}
