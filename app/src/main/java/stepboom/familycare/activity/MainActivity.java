package stepboom.familycare.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import stepboom.familycare.Contextor;
import stepboom.familycare.R;
import stepboom.familycare.service.FullscreenService;
import stepboom.familycare.service.TestService;
import stepboom.familycare.util.User;
import stepboom.familycare.util.UserParcelable;
import stepboom.familycare.adapter.CustomAdapter;
import stepboom.familycare.view.ExpandableHeightListView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private TextView searchText;
    private TextView searchText2;
    private TextView detailText;
    private TextView nameText;
    private TextView macText;
    private ImageView menuIcon;
    private ImageView searchIcon;
    private ImageView moreIcon;
    private DrawerLayout menu;
    private NavigationView navigationView;
    private Switch mSwitch;
    private FloatingActionsMenu fabMenu;
    private FloatingActionButton addMac;
    private FloatingActionButton addScan;
    private RelativeLayout fabOverlay;
    private BluetoothAdapter mBluetoothAdapter;
    private SharedPreferences sp;
    private SharedPreferences sp2;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editor2;
    private TextInputEditText userName;
    private TextInputEditText userDescription;
    private Spinner userRole;
    private boolean start = false;
    private int i = 0;

    private TextView enableMonitorTextView;
    private TextView disableMonitorTextView;
    private ExpandableHeightListView enableMonitorListView;
    private ExpandableHeightListView disableMonitorListView;
    private CustomAdapter enableAdapter;
    private CustomAdapter disableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initInstances();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /** request permission via start activity for result */
                startActivityForResult(intent, 104);
            }
        }

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, HomeFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }*/

        // --------------------------- Begin Custom List View ------------------------------------

        // ------ SharedPreference ------//
        String[] name_list = { "Phongpeeradej","Peerakit","Sitthichai", "Supakrit"};
        Integer[] role_list = {0,1,0,0}; // 0 = child , 1 = adult
        Integer[] status_list = {1,2,3,4};
        // ------ SharedPreference ------//

        // CustomAdapter resides in adapter folder
        /*CustomAdapter adapter = new CustomAdapter(getApplicationContext(),
                name_list,
                role_list,
                status_list);*/

        /*enableMonitorListView.setAdapter(adapter);
        disableMonitorListView.setAdapter(adapter);*/

        // --------------------------- End Custom List View ------------------------------------

    }

    private void initInstances() {
//        searchText = findViewById(R.id.main_search_text);
//        searchText2 = findViewById(R.id.main_search_text2);
        searchText = findViewById(R.id.main_text_topbar);
        menuIcon = findViewById(R.id.main_icon_hamburger);
        searchIcon = findViewById(R.id.main_icon_search);
        moreIcon = findViewById(R.id.main_icon_more);
        menu = findViewById(R.id.main_layout_drawer);
        addScan = findViewById(R.id.main_fab_add_scan);
        addMac = findViewById(R.id.main_fab_add_mac);
        fabOverlay = findViewById(R.id.main_fab_overlay);
        fabMenu = findViewById(R.id.main_fab_menu);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String macAddress = android.provider.Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");
        sp = getSharedPreferences(getString(R.string.shared_preferences_user), Context.MODE_PRIVATE);
        editor = sp.edit();
        sp2 = getSharedPreferences(getString(R.string.shared_preferences_member), Context.MODE_PRIVATE);
        editor2 = sp2.edit();

        /*editor2.putString("E8:2A:EA:DC:E7:C6","MSI/My Computer/Children/1");
        editor2.apply();*/

        /*Map<String, ?> allEntries = sp2.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().toString());
        }*/

        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                fabOverlay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.menu_overlay));
                fabOverlay.setClickable(false);
                fabOverlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(fabMenu.isExpanded()){
                            fabMenu.collapse();
                        }
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                fabOverlay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.transparent));
                fabOverlay.setClickable(false);
            }
        });



        addMac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_new_member_dialog);
                final TextInputEditText mac = dialog.findViewById(R.id.add_new_member_mac);
                mac.requestFocus();
                final TextInputEditText name = dialog.findViewById(R.id.add_new_member_name);
                final TextInputEditText description = dialog.findViewById(R.id.add_new_member_description);
                final Spinner spinner = dialog.findViewById(R.id.add_new_member_spinner);
                String [] roles = getResources().getStringArray(R.array.roles);
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line,roles);
                spinner.setAdapter(spinnerAdapter);
                Button done = dialog.findViewById(R.id.add_new_member_done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String detail = name.getText().toString() + "/" + description.getText().toString() + "/"
                                + spinner.getSelectedItem() + "/0";
                        editor2.putString(mac.getText().toString(), detail);
                        editor2.apply();
                        dialog.cancel();
                        setListAdapter();
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
        });

        addScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBluetooth();
            }
        });

        enableMonitorTextView = findViewById(R.id.tvEnableMonitor);
        disableMonitorTextView = findViewById(R.id.tvDisableMonitor);
        enableMonitorListView = findViewById(R.id.lvEnableMonitor);
        disableMonitorListView = findViewById(R.id.lvDisableMonitor);

        enableMonitorListView.setExpanded(true);
        disableMonitorListView.setExpanded(true);
        enableMonitorListView.setFocusable(false);
        disableMonitorListView.setFocusable(false);

        navigationView = findViewById(R.id.main_navigation);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navItem1:
                        break;
                    case R.id.navItem2:
                        break;
                    case R.id.navItem3:
                        break;
                }
                return true;
            }
        });

        navigationView.getMenu().getItem(0).setChecked(true);

        View header = navigationView.getHeaderView(0);
        mSwitch = header.findViewById(R.id.nav_switch);
        detailText = header.findViewById(R.id.nav_text_status);
        nameText = header.findViewById(R.id.nav_text_name);
        macText = header.findViewById(R.id.nav_text_mac);

        if(sp.contains("protected")){
            if(sp.getBoolean("protected",false)){
                bluetoothOn();
            } else {
                bluetoothOff();
            }
        } else {
            editor.putBoolean("protected",false);
            editor.apply();
        }

        if(sp.contains(getString(R.string.shared_preferences_name)))
            nameText.setText(sp.getString(getString(R.string.shared_preferences_name),getString(R.string.empty_string)));
        else nameText.setText("Netty");
        macText.setText(macAddress);

        nameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.edit_user_dialog);
                userName = dialog.findViewById(R.id.add_new_member_name);
                if(sp.contains(getString(R.string.shared_preferences_name)))
                    userName.setText(sp.getString(getString(R.string.shared_preferences_name),getString(R.string.empty_string)));
                userDescription = dialog.findViewById(R.id.add_new_member_description);
                if(sp.contains(getString(R.string.shared_preferences_description)))
                    userDescription.setText(sp.getString(getString(R.string.shared_preferences_description),getString(R.string.empty_string)));
                userRole = dialog.findViewById(R.id.add_new_member_spinner);
                String [] roles = getResources().getStringArray(R.array.roles);
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line,roles);
                userRole.setAdapter(spinnerAdapter);
                if(sp.contains(getString(R.string.shared_preferences_role)))
                    userRole.setSelection(sp.getString(getString(R.string.shared_preferences_role),getString(R.string.empty_string))
                            .equals(getString(R.string.parents)) ? 0 : 1);
                Button done = dialog.findViewById(R.id.add_new_member_done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(userName.getText().toString().length() >= 1) {
                            nameText.setText(userName.getText().toString());
                            editor.putString(getString(R.string.shared_preferences_name), userName.getText().toString());
                        }
                        if(userDescription.getText().toString().length() >= 1)
                            editor.putString(getString(R.string.shared_preferences_description),userDescription.getText().toString());
                        editor.putString(getString(R.string.shared_preferences_role),userRole.getSelectedItem().toString());
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
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 100);
                } else {
                    bluetoothOn();
                }
            } else {
                bluetoothOff();
            /*if (mBluetoothAdapter.isEnabled()){
                mBluetoothAdapter.disable();
            }*/
            }
            }
        });

//        searchText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent search = new Intent(getApplicationContext(), SearchActivity.class);
//                search.putExtra("user", new UserParcelable("E8:2A:EA:DC:E7:C6","MSI/My Computer/Children/4"));
//                Intent i = new Intent(getApplicationContext(), TestService.class);
//                stopService(i);
//                startActivityForResult(search,103);
//                /*if(!start) {*/
//                    /*Intent i = new Intent(getApplicationContext(), TestService.class);
//                    i.putExtra("KEY1", "Value to be used by the service");
//                    startService(i);*/
//                /*}else{
//                    Intent i = new Intent(getApplicationContext(), TestService.class);
//                    stopService(i);
//                }*/
//            }
//        });
//
//        searchText2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), FullscreenService.class);
//                startService(i);
//                /*Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
//                startActivityForResult(intent, 102);*/
//                /*Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                Notification n  = new Notification.Builder(MainActivity.this)
//                        .setContentTitle("FamilyCare Notification")
//                        .setContentText("Hello From FamilyCare")
//                        .setSmallIcon(R.drawable.ic_shield)
//                        .setContentIntent(pIntent)
//                        .setAutoCancel(true)
//                        .setFullScreenIntent(pIntent, true).getNotification();*/
//                        /*.addAction(R.drawable.icon, "Call", pIntent)
//                        .addAction(R.drawable.icon, "More", pIntent)
//                        .addAction(R.drawable.icon, "And more", pIntent).build();*/
//
//
//                /*NotificationManager notificationManager =
//                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                notificationManager.notify(0, n);*/
//
//            }
//        });
        menuIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                menu.openDrawer(Gravity.LEFT);
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /*do something*/
            }
        });

        moreIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(),moreIcon);
                popupMenu.getMenuInflater().inflate(R.menu.navigation_more_items, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.more_item_1 : {
                                Intent intent = new Intent(getApplicationContext(),
                                        SetMonitorActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case R.id.more_item_2 : {
                                break;
                            }
                            case R.id.more_item_3 : {
                                break;
                            }
                            case R.id.more_item_4 : {
                                break;
                            }
                        }
                        Toast.makeText(getApplicationContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void scanBluetooth() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 101);
        } else {
            fabMenu.collapse();
            Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            //Opening Bluetooth
            case 100 : {
                if(resultCode==RESULT_OK){
                    bluetoothOn();
                } else if(resultCode==RESULT_CANCELED){
                    mSwitch.setChecked(false);
                    bluetoothOff();
                }
                break;
            }
            //Opening Search Bluetooth Activity
            case 101 : {
                if(resultCode==RESULT_OK){
                    Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                    startActivity(intent);
                } else if(resultCode==RESULT_CANCELED){
                    Toast.makeText(getApplicationContext(),getString(R.string.main_activity_cannot_open_bluetooth), Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case 102 :{
                if(resultCode==RESULT_OK) {
                    Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                } else if (resultCode==RESULT_CANCELED){
                    Toast.makeText(getApplicationContext(),"Failure", Toast.LENGTH_LONG).show();
                }
            }
            //Search Activity
            case 103: {
                if(sp.getBoolean("protected",false)){
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent i = new Intent(getApplicationContext(), TestService.class);
                            startService(i);
                        }
                    },1000);
                }
                break;
            }
            //Opening Overlay Permission
            case 104 : {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(this)) {
                        // continue here - permission was granted
                        Toast.makeText(getApplicationContext(),"Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    @Override
    public void onResume(){
        setListAdapter();
        super.onResume();
    }

    private void setListAdapter() {
        Map<String, ?> allEntries = sp2.getAll();
        ArrayList<User> userEnableList = new ArrayList<>();
        ArrayList<User> userDisableList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            User user = new User(entry.getKey(), entry.getValue().toString());
            if(user.getStatus().equals("0")) {
                userDisableList.add(user);
            } else {
                userEnableList.add(user);
            }
            System.out.println(entry.getKey() + " : " + entry.getValue().toString());
        }

        enableAdapter = new CustomAdapter(MainActivity.this, userEnableList);
        disableAdapter = new CustomAdapter(MainActivity.this, userDisableList);
        enableMonitorListView.setAdapter(enableAdapter);
        disableMonitorListView.setAdapter(disableAdapter);
    }

    private void bluetoothOn(){
        mSwitch.setChecked(true);
        detailText.setText("ON");
        detailText.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
        editor.putBoolean("protected",true);
        editor.apply();
        Intent i = new Intent(getApplicationContext(), TestService.class);
        startService(i);
    }

    private void bluetoothOff(){
        mSwitch.setChecked(false);
        detailText.setText("OFF");
        detailText.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        editor.putBoolean("protected",false);
        editor.apply();
        Intent i = new Intent(getApplicationContext(), TestService.class);
        stopService(i);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed(){
        if(fabMenu.isExpanded()){
            fabMenu.collapse();
        } else {
            super.onBackPressed();
        }
    }
}
