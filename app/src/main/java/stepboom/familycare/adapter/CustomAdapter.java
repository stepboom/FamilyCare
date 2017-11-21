package stepboom.familycare.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import stepboom.familycare.Contextor;
import stepboom.familycare.R;
import stepboom.familycare.activity.ScanActivity;
import stepboom.familycare.activity.SearchActivity;
import stepboom.familycare.service.TestService;
import stepboom.familycare.util.User;

/**
 * Created by netklause on 11/13/2017 AD.
 */

public class CustomAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<User> users;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

   public CustomAdapter(Context context, ArrayList<User> users){
       this.mContext = context;
       this.users = users;
       sp = mContext.getSharedPreferences(mContext.getString(R.string.shared_preferences_member), Context.MODE_PRIVATE);
       editor = sp.edit();
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

        view = mInflater.inflate(R.layout.list_view_row, parent, false);

        ImageView humanIcon = view.findViewById(R.id.humanIcon);
        TextView textView = view.findViewById(R.id.humanName);
        textView.setText(users.get(position).getName());
        ImageView imageView = view.findViewById(R.id.statusIcon);

        if (users.get(position).getStatus().equals("0")) {
            if (users.get(position).getRole().equals("Children")) {
                humanIcon.setBackgroundResource(R.drawable.ic_child_disable);
            } else if (users.get(position).getRole().equals("Parent")) {
                humanIcon.setBackgroundResource(R.drawable.ic_adult_disable);
            }
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
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
                    search.putExtra("macAddress", users.get(position).getMacAddress());
                    Intent i = new Intent(mContext, TestService.class);
                    mContext.stopService(i);
                    ((Activity) mContext).startActivityForResult(search, 103);
                } else {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.add_new_member_dialog);
                    final TextInputEditText mac = dialog.findViewById(R.id.add_new_member_mac);
                    mac.setText(users.get(position).getMacAddress());
                    mac.setEnabled(false);
                    final TextInputEditText name = dialog.findViewById(R.id.add_new_member_name);
                    name.setText(users.get(position).getName());
                    name.requestFocus();
                    final TextInputEditText description = dialog.findViewById(R.id.add_new_member_description);
                    final Spinner spinner = dialog.findViewById(R.id.add_new_member_spinner);
                    String[] roles = mContext.getResources().getStringArray(R.array.roles);
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_dropdown_item_1line, roles);
                    spinner.setAdapter(spinnerAdapter);
                    Button done = dialog.findViewById(R.id.add_new_member_done);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String detail = name.getText().toString() + "/" + description.getText().toString() + "/"
                                    + spinner.getSelectedItem() + "/0";
                            editor.putString(mac.getText().toString(), detail);
                            editor.apply();
                            dialog.cancel();
                        }
                    });
                    Button cancel = dialog.findViewById(R.id.add_new_member_cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
