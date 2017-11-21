package stepboom.familycare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import stepboom.familycare.R;
import stepboom.familycare.util.User;

/**
 * Created by netklause on 11/21/2017 AD.
 */

public class CustomMonitorAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<User> users;

    public CustomMonitorAdapter(Context mContext, ArrayList<User> users) {
        this.mContext = mContext;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {

        final int position = pos;
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null) {
            view = mInflater.inflate(R.layout.list_view_row, parent, false);

            Switch mSwitch = view.findViewById(R.id.monitorSwitch);
            ImageView humanIcon = view.findViewById(R.id.humanIcon_monitor);
            TextView textView = view.findViewById(R.id.humanName_monitor);
            textView.setText(users.get(position).getName());
        }

        System.out.println("hello");


        return view;
    }
}
