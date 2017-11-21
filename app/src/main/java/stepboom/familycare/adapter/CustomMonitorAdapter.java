package stepboom.familycare.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import stepboom.familycare.Contextor;
import stepboom.familycare.R;
import stepboom.familycare.util.User;

/**
 * Created by netklause on 11/21/2017 AD.
 */

public class CustomMonitorAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<User> users;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public CustomMonitorAdapter(Context mContext, ArrayList<User> users) {
        this.mContext = mContext;
        this.users = users;

        sp = mContext.getSharedPreferences(mContext.getString(R.string.shared_preferences_member), Context.MODE_PRIVATE);
        editor = sp.edit();

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
    public View getView(final int pos, View view, ViewGroup parent) {

        final int position = pos;
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = mInflater.inflate(R.layout.list_view_set_monitor, parent, false);

        Switch mSwitch = view.findViewById(R.id.monitorSwitch);
        ImageView humanIcon = view.findViewById(R.id.humanIcon_monitor);
        TextView textView = view.findViewById(R.id.humanName_monitor);
        textView.setText(users.get(position).getName());

        mSwitch.setChecked(!users.get(position).getStatus().equals("0"));

        if(users.get(position).getStatus().equals("0")){
            if (users.get(position).getRole().equals("Children")) {
                humanIcon.setBackgroundResource(R.drawable.ic_child_disable);
            } else if (users.get(position).getRole().equals("Parent")) {
                humanIcon.setBackgroundResource(R.drawable.ic_adult_disable);
            }
            textView.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
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
        }
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, boolean b) {
                if(b){
                    users.get(pos).setStatus("1");
                    editor.putString(users.get(position).getMacAddress(),users.get(position).getInformation());
                    editor.apply();
                } else{
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.confirm_dialog);
                    Button done = dialog.findViewById(R.id.disable_member_done);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            users.get(pos).setStatus("0");
                            editor.putString(users.get(position).getMacAddress(),users.get(position).getInformation());
                            editor.apply();
                            dialog.cancel();
                        }
                    });
                    Button cancel = dialog.findViewById(R.id.disable_member_cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            compoundButton.setChecked(true);
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            }
        });

        return view;
    }
}
