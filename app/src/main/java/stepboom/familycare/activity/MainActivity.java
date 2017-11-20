package stepboom.familycare.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import stepboom.familycare.Contextor;
import stepboom.familycare.R;
import stepboom.familycare.adapter.CustomAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private TextView searchText;
    private ImageView menuIcon;
    private ImageView searchIcon;
    private ImageView moreIcon;
    private DrawerLayout menu;
    private NavigationView navigationView;
    private Switch mSwitch;
    private TextView detailText;

    private TextView enableMonitorTextView;
    private TextView disableMonitorTextView;
    private ListView enableMonitorListView;
    private ListView disableMonitorListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();

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
        CustomAdapter adapter = new CustomAdapter(getApplicationContext(),
                name_list,
                role_list,
                status_list);

        enableMonitorListView.setAdapter(adapter);
        disableMonitorListView.setAdapter(adapter);

        // --------------------------- End Custom List View ------------------------------------

    }

    private void initInstances() {

        searchText = findViewById(R.id.main_text_topbar);
        menuIcon = findViewById(R.id.main_icon_hamburger);
        searchIcon = findViewById(R.id.main_icon_search);
        moreIcon = findViewById(R.id.main_icon_more);
        menu = findViewById(R.id.main_layout_drawer);

        enableMonitorTextView = findViewById(R.id.tvEnableMonitor);
        disableMonitorTextView = findViewById(R.id.tvDisableMonitor);
        enableMonitorListView = findViewById(R.id.lvEnableMonitor);
        disableMonitorListView = findViewById(R.id.lvDisableMonitor);


        navigationView = findViewById(R.id.main_navigation);
        View header = navigationView.getHeaderView(0);
        mSwitch = header.findViewById(R.id.nav_switch);
        detailText = header.findViewById(R.id.nav_text_status);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    detailText.setText("ON");
                    detailText.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
                } else {
                    detailText.setText("OFF");
                    detailText.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                }
            }
        });

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                startActivity(intent);
            }
        });
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
                /*do something*/
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
