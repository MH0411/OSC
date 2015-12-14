package com.example.lenovo.osc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Lenovo on 13/11/2015.
 */
public class SaleAdapter extends ArrayAdapter<ParseObject> {

    private Context context;
    private List<ParseObject> saleList;

    public SaleAdapter(Context context, List<ParseObject> saleList) {

        super(context, R.layout.sale_list_single_view, saleList);

        this.context = context;
        this.saleList = saleList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.sale_list_single_view, null);
            holder = new ViewHolder();
            holder.tvSaleID = (TextView) convertView.findViewById(R.id.tvSaleID);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tvSaleDate);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvSaleID.setText(saleList.get(position).getObjectId());
        holder.tvDate.setText(saleList.get(position).getDate("DateTime").toString());
        return convertView;
    }

    public static class ViewHolder {
        TextView tvSaleID;
        TextView tvDate;
    }
}
