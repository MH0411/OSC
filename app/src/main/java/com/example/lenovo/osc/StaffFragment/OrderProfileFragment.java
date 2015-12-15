package com.example.lenovo.osc.StaffFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.osc.Order.Order;
import com.example.lenovo.osc.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.Date;

/**
 * Created by Lenovo on 4/12/2015.
 */
public class OrderProfileFragment extends Fragment implements View.OnClickListener{

    protected TextView tvOrderObjectID;
    protected TextView tvOrderName;
    protected TextView tvOrderQuantity;
    protected TextView tvOrderAmount;
    protected TextView tvOrderDate;
    protected TextView tvReceiveDate;
    protected ImageView ivOrderProfileStockImage;
    protected Button bOk;

    private Order order;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_profile_layout, container, false);

        tvOrderObjectID = (TextView) view.findViewById(R.id.tvOrderProfileID);
        tvOrderName = (TextView) view.findViewById(R.id.tvOrderProfileName);
        tvOrderQuantity = (TextView) view.findViewById(R.id.tvOrderProfileQuantity);
        tvOrderAmount = (TextView) view.findViewById(R.id.tvOrderProfileAmount);
        tvOrderDate = (TextView) view.findViewById(R.id.tvOrderProfileOrderDate);
        tvReceiveDate = (TextView) view.findViewById(R.id.tvOrderProfileReceiveDate);
        ivOrderProfileStockImage = (ImageView) view.findViewById(R.id.ivOrderProfileStockImage);
        bOk = (Button) view.findViewById(R.id.bOrderOk);
        bOk.setOnClickListener(this);

        order = new Order(
                getArguments().getString("objectID"),
                getArguments().getString("name"),
                getArguments().getInt("quantity", 0),
                getArguments().getDouble("amount", 0),
                getArguments().getString("orderDate"),
                getArguments().getString("receiveDate")
        );

        tvOrderObjectID.setText(order.getObjectId());
        tvOrderName.setText(order.getName());
        tvOrderQuantity.setText(String.valueOf(order.getQuantity()));
        tvOrderAmount.setText(String.valueOf(order.getAmount()));
        tvOrderDate.setText(order.getOrderDate().toString());
        if (order.getReceiveDate() == null) {
            tvReceiveDate.setText("In Progress.");
            bOk.setText("Receive");
        } else
            tvReceiveDate.setText(order.getReceiveDate().toString());

        //Load the selected stock's image.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CentreOrder");
        query.include("CentreStockObjectID");
        query.getInBackground(order.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Picasso.with(getActivity().getApplicationContext()).
                            load(object.getParseObject("CentreStockObjectID").getParseFile("Image").
                                    getUrl()).noFade().into(ivOrderProfileStockImage);
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bOrderOk:
                if (order.getReceiveDate() == null){

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("CentreOrder");
                    query.getInBackground(order.getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, com.parse.ParseException e) {
                            if (e == null){

                                object.put("ReceiveDate", new Date());
                                object.saveInBackground();
                                Toast.makeText(getActivity(), "Order received.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                getFragmentManager().beginTransaction().
                        replace(R.id.container, new OrdersListFragment()).addToBackStack(null).commit();
                break;
        }
    }
}
