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
public class StockOnSaleAdapter extends ArrayAdapter<ParseObject> {

    private Context context;
    private List<ParseObject> stockList;
    private DecimalFormat df = new DecimalFormat("#.00");

    public StockOnSaleAdapter(Context context, List<ParseObject> stockList) {

        super(context, R.layout.stock_on_sale_list_single_view, stockList);

        this.context = context;
        this.stockList = stockList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.stock_on_sale_list_single_view, null);
            holder = new ViewHolder();
            holder.stockImage = (ImageView) convertView.findViewById(R.id.ivStockOnSaleImage);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvStockOnSaleName);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tvStockOnSalePrice);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject object = stockList.get(position);
        //get the image
        Picasso.with(getContext().getApplicationContext()).load(object.getParseFile("Image").
                getUrl()).noFade().into(holder.stockImage);
        holder.tvName.setText(stockList.get(position).getString("Name"));
        holder.tvPrice.setText("RM" + df.format(stockList.get(position).getDouble("Price")));
        return convertView;
    }

    public static class ViewHolder {
        ImageView stockImage;
        TextView tvName;
        TextView tvPrice;
    }
}
