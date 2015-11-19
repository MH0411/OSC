package com.example.lenovo.osc.Users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lenovo.osc.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 13/11/2015.
 */
public class UserAdapter extends ArrayAdapter<User> {

    private final Context context;
    private final ArrayList<User> usersArrayList;

    public UserAdapter(Context context, ArrayList<User> usersArrayList) {

        super(context, R.layout.user_list_view, usersArrayList);

        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.user_list_view, parent, false);

        // 3. Get the two text view from the rowView
        TextView textView = (TextView) rowView.findViewById(R.id.tvListUserID);
        TextView valueView = (TextView) rowView.findViewById(R.id.tvListName);

        // 4. Set the text for textView
        textView.setText(usersArrayList.get(position).getUserID());
        valueView.setText(usersArrayList.get(position).getName());

        // 5. retrn rowView
        return rowView;
    }
}
