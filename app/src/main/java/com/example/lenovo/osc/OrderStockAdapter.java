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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 13/11/2015.
 */
public class OrderStockAdapter extends ArrayAdapter<ParseObject> {

    private Context context;
    private ArrayList<ParseObject> stockList;
    private ArrayList<ParseObject> stocks;
    private ArrayList quantity;
    private DecimalFormat df = new DecimalFormat("#.00");

    public OrderStockAdapter(Context context, List<ParseObject> stockList) {

        super(context, R.layout.orders_list_single_view, stockList);

        this.context = context;
        this.stockList = (ArrayList<ParseObject>) stockList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        stocks = stockList;

        final ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.orders_list_single_view, null);
            holder = new ViewHolder();
            holder.stockImage = (ImageView) convertView.findViewById(R.id.ivOrderListImage);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvOrderListOrderID);
            holder.tvQuantity = (TextView) convertView.findViewById(R.id.tvOrderListQuantity);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject object = stockList.get(position);
        //get the image
        Picasso.with(getContext().getApplicationContext()).load(object.getParseFile("Image").
                getUrl()).noFade().into(holder.stockImage);
        holder.tvName.setText(stockList.get(0).getObjectId());
        holder.tvQuantity.setText(Integer.toString(stockList.get(0).getInt("Quantity")));
        return convertView;
    }

    public static class ViewHolder {
        ImageView stockImage;
        TextView tvName;
        TextView tvQuantity;
    }
}
