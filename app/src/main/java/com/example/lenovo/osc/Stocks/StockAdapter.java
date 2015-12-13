package com.example.lenovo.osc.Stocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.osc.R;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Lenovo on 13/11/2015.
 */
public class StockAdapter extends ArrayAdapter<ParseObject> {

    private Context context;
    private List<ParseObject> stockList;
    private DecimalFormat df = new DecimalFormat("#.00");

    public StockAdapter(Context context, List<ParseObject> stockList) {

        super(context, R.layout.stocks_list_single_view, stockList);

        this.context = context;
        this.stockList = stockList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.stocks_list_single_view, null);
            holder = new ViewHolder();
            holder.stockImage = (ImageView) convertView.findViewById(R.id.ivStockPicture);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvStockName);
            holder.tvCost = (TextView) convertView.findViewById(R.id.tvStockCost);
            holder.tvQuantity = (TextView) convertView.findViewById(R.id.tvStockQuantity);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject object = stockList.get(position);
        //get the image
        Picasso.with(getContext().getApplicationContext()).load(object.getParseFile("Image").getUrl()).noFade().into(holder.stockImage);
        holder.tvName.setText(stockList.get(position).getString("Name"));
        holder.tvCost.setText("RM" + df.format(stockList.get(position).getDouble("Cost")));
        holder.tvQuantity.setText(Integer.toString(stockList.get(position).getInt("Quantity")));
        return convertView;
    }

    public static class ViewHolder {
        ImageView stockImage;
        TextView tvName;
        TextView tvCost;
        TextView tvQuantity;
    }
}
