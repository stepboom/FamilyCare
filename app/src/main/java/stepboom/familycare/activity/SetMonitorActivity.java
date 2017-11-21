package stepboom.familycare.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import stepboom.familycare.R;
import stepboom.familycare.adapter.CustomMonitorAdapter;
import stepboom.familycare.util.User;

public class SetMonitorActivity extends AppCompatActivity {

    private ImageView crossButton;
    private Switch mSwitch;
    private TextView setMonitorTextView;
    private ListView setMonitorListView;
    private CustomMonitorAdapter monitorAdapter;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_monitor);

        initInstances();

    }

    private void initInstances() {
        sp = getSharedPreferences(getString(R.string.shared_preferences_member), Context.MODE_PRIVATE);
        editor = sp.edit();

        setMonitorListView = findViewById(R.id.monitor_Listview);
        mSwitch = findViewById(R.id.monitorSwitch);
        crossButton = findViewById(R.id.cross_set_monitor);
        crossButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        setListAdapter();
    }

    private void setListAdapter() {
        Map<String, ?> allEntries = sp.getAll();
        ArrayList<User> userMonitorList = new ArrayList<>();
        ArrayList<User> userMonitorList2 = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            User user = new User(entry.getKey(), entry.getValue().toString());
            if(user.getStatus().equals("0")) {
                userMonitorList2.add(user);
            } else {
                userMonitorList.add(user);
            }
            System.out.println(entry.getKey() + " : " + entry.getValue().toString());
        }

        if(userMonitorList.size() > 0)
            for(int i =  0 ; i < userMonitorList2.size() ; i++) {
                userMonitorList.add(userMonitorList2.get(i));
            }
        else {
            userMonitorList = userMonitorList2;
        }

        System.out.println(userMonitorList.size());
        System.out.println(userMonitorList2.size());

        monitorAdapter = new CustomMonitorAdapter(SetMonitorActivity.this, userMonitorList);
        setMonitorListView.setAdapter(monitorAdapter);
    }
}
