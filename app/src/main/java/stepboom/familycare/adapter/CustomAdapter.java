package stepboom.familycare.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import stepboom.familycare.Contextor;
import stepboom.familycare.R;
import stepboom.familycare.activity.SearchActivity;
import stepboom.familycare.service.TestService;
import stepboom.familycare.util.User;

/**
 * Created by netklause on 11/13/2017 AD.
 */

public class CustomAdapter extends BaseAdapter {

    //NETTO VERSION
   /* Context mContext;
    String[] strName;
    Integer[] role;
    Integer[] status;


    public CustomAdapter(Context context, String[] strName, Integer[] role, Integer[] status) {
        this.mContext= context;
        this.strName = strName;
        this.status = status;
        this.role = role; // 0 = child , 1 = adult
    }*/

   private Context mContext;
   private ArrayList<User> users;

   public CustomAdapter(Context context, ArrayList<User> users){
       this.mContext = context;
       this.users = users;
   }

    public int getCount() {
        return users.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        final int position = pos;
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.list_view_row, parent, false);


        ImageView humanIcon = view.findViewById(R.id.humanIcon);
        TextView textView = view.findViewById(R.id.humanName);
        textView.setText(users.get(position).getName());
        ImageView imageView = view.findViewById(R.id.statusIcon);

        if(users.get(position).getStatus().equals("0")){
            if (users.get(position).getRole().equals("Children")) {
                humanIcon.setBackgroundResource(R.drawable.ic_child_disable);
            } else if (users.get(position).getRole().equals("Parent")) {
                humanIcon.setBackgroundResource(R.drawable.ic_adult_disable);
            }
            textView.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
            imageView.setBackgroundResource(R.color.transparent);
        } else {
            if (users.get(position).getRole().equals("Children")) {
                if (users.get(position).getStatus().equals("1")
                        || users.get(position).getStatus().equals("2")) {
                    humanIcon.setBackgroundResource(R.drawable.ic_child_normal);
                } else {
                    humanIcon.setBackgroundResource(R.drawable.ic_child_lost);
                }
            } else if (users.get(position).getRole().equals("Parent")) {
                if (users.get(position).getStatus().equals("1")
                        || users.get(position).getStatus().equals("2")) {
                    humanIcon.setBackgroundResource(R.drawable.ic_adult_normal);
                } else {
                    humanIcon.setBackgroundResource(R.drawable.ic_adult_lost);
                }
            }

            if (users.get(position).getStatus().equals("3") || users.get(position).getStatus().equals("4")) {
                textView.setTextColor(ContextCompat.getColor(Contextor.getInstance().getContext(), R.color.red));
            } else {
                textView.setTextColor(ContextCompat.getColor(Contextor.getInstance().getContext(), R.color.black));
            }


            if (users.get(position).getStatus().equals("3") || users.get(position).getStatus().equals("4")) {
                imageView.setBackgroundResource(R.drawable.ic_status);
            } else {
                imageView.setBackgroundResource(R.color.transparent);
            }
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (users.get(position).getStatus().equals("3") || users.get(position).getStatus().equals("4")) {
                    Intent search = new Intent(mContext, SearchActivity.class);
                    search.putExtra("macAddress",users.get(position).getMacAddress());
                    Intent i = new Intent(mContext, TestService.class);
                    mContext.stopService(i);
                    ((Activity) mContext).startActivityForResult(search,103);
                }
            }
        });

        return view;
    }
}
