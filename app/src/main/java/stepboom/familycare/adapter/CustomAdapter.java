package stepboom.familycare.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import stepboom.familycare.Contextor;
import stepboom.familycare.R;

/**
 * Created by netklause on 11/13/2017 AD.
 */

public class CustomAdapter extends BaseAdapter {

    Context mContext;
    String[] strName;
    Integer[] role;
    Integer[] status;


    public CustomAdapter(Context context, String[] strName, Integer[] role, Integer[] status) {
        this.mContext= context;
        this.strName = strName;
        this.status = status;
        this.role = role; // 0 = child , 1 = adult
    }

    public int getCount() {
        return strName.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.list_view_row, parent, false);


        // ---------------------- human's icon ----------------------
        ImageView humanIcon = (ImageView)view.findViewById(R.id.humanIcon);

        if(role[position] == 0 && (status[position] == 0 || status[position] == 1 || status[position] == 2)) {
            // if a child is normal
            humanIcon.setBackgroundResource(R.drawable.ic_child_normal);

        }

        if(role[position] == 0 && (status[position] == 3 || status[position] == 4)) {
            // if a child is lost
            humanIcon.setBackgroundResource(R.drawable.ic_child_lost);

        }

        if(role[position] == 1) {
            // if this is adult
            humanIcon.setBackgroundResource(R.drawable.ic_adult_normal);
        }

        // ---------------------- human's icon ----------------------




        // ---------------------- human's name ----------------------
        TextView textView = (TextView)view.findViewById(R.id.humanName);
        textView.setText(strName[position]);
        if(status[position] == 3 || status[position] == 4)
            textView.setTextColor(ContextCompat.getColor(Contextor.getInstance().getContext(), R.color.red));

        Contextor.getInstance().getContext();






        // ---------------------- status icon ----------------------
        /// if(status[position] == 2 || status[position] == 2) {
        // display status lost when children is lost , otherwise not display this imageview

        ImageView imageView = (ImageView)view.findViewById(R.id.statusIcon);
        imageView.setBackgroundResource(R.drawable.ic_status);


        /// }
        // ---------------------- status icon ----------------------


        return view;
    }
}
