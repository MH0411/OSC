package com.example.lenovo.osc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Lenovo on 13/11/2015.
 */
public class OrderAdapter extends ArrayAdapter<ParseObject> {

    private Context context;
    private List<ParseObject> orderList;
    private DecimalFormat df = new DecimalFormat("#.00");

    public OrderAdapter(Context context, List<ParseObject> orderList) {

        super(context, R.layout.orders_list_view, orderList);

        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.orders_list_view, null);
            holder = new ViewHolder();
            holder.stockImage = (ImageView) convertView.findViewById(R.id.ivOrderListImage);
            holder.tvOrderName = (TextView) convertView.findViewById(R.id.tvOrderListName);
            holder.tvOrderQuantity = (TextView) convertView.findViewById(R.id.tvOrderListQuantity);
            holder.tvOrderID = (TextView) convertView.findViewById(R.id.tvOrderListOrderID);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject object = orderList.get(position);
        //get the image
        Picasso.with(getContext().getApplicationContext()).load(object.getParseObject("CentreStockObjectID").getParseFile("Image").getUrl()).noFade().into(holder.stockImage);
        holder.tvOrderName.setText(orderList.get(position).getParseObject("CentreStockObjectID").getString("Name"));
        holder.tvOrderQuantity.setText(String.valueOf(orderList.get(position).getInt("Quantity")));
        holder.tvOrderID.setText(orderList.get(position).getObjectId());
        return convertView;
    }

    public static class ViewHolder {
        ImageView stockImage;
        TextView tvOrderName;
        TextView tvOrderQuantity;
        TextView tvOrderID;
    }
}