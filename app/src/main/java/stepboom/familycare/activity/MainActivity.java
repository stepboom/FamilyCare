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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import stepboom.familycare.Contextor;
import stepboom.familycare.R;
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
    }

    private void initInstances() {
        searchText = findViewById(R.id.main_search_text);
        menuIcon = findViewById(R.id.main_icon_hamburger);
        searchIcon = findViewById(R.id.main_icon_search);
        moreIcon = findViewById(R.id.main_icon_more);
        menu = findViewById(R.id.main_layout_drawer);

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
