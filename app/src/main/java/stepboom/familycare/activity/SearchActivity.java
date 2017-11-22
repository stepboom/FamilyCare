package stepboom.familycare.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import stepboom.familycare.R;
import stepboom.familycare.service.TestService;
import stepboom.familycare.util.User;
import stepboom.familycare.util.UserParcelable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private ImageView icon;
    private TextView name;
    private TextView status;
    private TextView description;
    private TextView rssi;
    private Button button;
    private User user;
    private BluetoothAdapter mBluetoothAdapter;
    private Vibrator vibrator;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private boolean finished;
    private Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        /*UserParcelable userParcelable = getIntent().getParcelableExtra("user");
        user = new User(userParcelable.getMacAddress(),userParcelable.getInformation());*/
        initInstances();
        doDiscovery();
    }

    private void initInstances() {
        layout = findViewById(R.id.search_layout);
        icon = findViewById(R.id.search_image_status);
        name = findViewById(R.id.search_text_name);
        status = findViewById(R.id.search_text_lost);
        button = findViewById(R.id.search_button_close);
        description = findViewById(R.id.search_text_description);
        rssi = findViewById(R.id.search_text_rssi);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        sp = getSharedPreferences(getString(R.string.shared_preferences_member), Context.MODE_PRIVATE);
        editor = sp.edit();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        String macAddress = getIntent().getStringExtra("macAddress");
        user = new User(macAddress,sp.getString(macAddress,""));

        finished = false;

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        name.setText(user.getName());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("BEFORE CLOSE " + sp.getString(user.getMacAddress(), ""));
                finished = true;
                finish();
            }
        });
    }

    private void doDiscovery(){
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Final 7 Second");
                if(mBluetoothAdapter.isDiscovering()){
                    mBluetoothAdapter.cancelDiscovery();
                }
                mBluetoothAdapter.startDiscovery();
            }
        },0,7000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        System.out.println("Search Destroyed");

        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    private void found(){
        layout.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
        icon.setImageDrawable(getResources().getDrawable((R.drawable.ic_found)));
        name.setTextColor(ContextCompat.getColor(this,R.color.green));
        status.setBackgroundColor(ContextCompat.getColor(this,R.color.green));
        status.setText("FOUND");
        button.setText("Back to Home Page");
        button.setTextColor(ContextCompat.getColor(this,R.color.white));
        button.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
        description.setTextColor(ContextCompat.getColor(this,R.color.red));
    }

    private void notFound(){
        layout.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
        icon.setImageDrawable(getResources().getDrawable((R.drawable.ic_searching)));
        name.setTextColor(ContextCompat.getColor(this,R.color.white));
        status.setBackgroundColor(ContextCompat.getColor(this,R.color.dark_red));
        status.setText("SEARCHING");
        rssi.setText("");
        button.setText("Give Up");
        button.setTextColor(ContextCompat.getColor(this,R.color.red));
        button.setBackgroundResource(android.R.drawable.btn_default);
        description.setTextColor(ContextCompat.getColor(this,R.color.white));
    }

    private void vibrate(){
        vibrator.vibrate(new long[]{0,500,500,500},-1);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!finished) {
                String action = intent.getAction();
                final int rssiValue = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getAddress().equals(user.getMacAddress())) {
                        System.out.println("SEARCH FOUND : " + user.getMacAddress() + " " + user.getInformation());
                        vibrate();
                        rssi.setText("Found with Signal : " + rssiValue + " dBm");
                        user.setStatus("2");
                        editor.putString(user.getMacAddress(), user.getInformation());
                        editor.apply();
                        found();
                    }

                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    System.out.println("SEARCH FINISH : " + user.getMacAddress() + " " + user.getInformation());
                    if (user.getStatus().equals("2")) {
                        user.setStatus("1");
                        editor.putString(user.getMacAddress(), user.getInformation());
                        editor.apply();
                    } else if (user.getStatus().equals("1") || user.getStatus().equals("3")) {
                        user.setStatus("3");
                        editor.putString(user.getMacAddress(), user.getInformation());
                        editor.apply();
                        vibrate();
                        notFound();
                    }
                }
            }
        }
    };

    /*@Override
    public void onBackPressed(){
        SharedPreferences sp2 = getSharedPreferences(getString(R.string.shared_preferences_user),MODE_PRIVATE);
        if(sp2.getBoolean("protected",false)) {
            Intent i = new Intent(getApplicationContext(), TestService.class);
            startService(i);
        }
        super.onBackPressed();
    }*/

    @Override
    public void onStop(){
        if(t!=null){
            t.cancel();
            t.purge();
        }
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

