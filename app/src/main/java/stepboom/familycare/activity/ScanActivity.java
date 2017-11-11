package stepboom.familycare.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

import stepboom.familycare.Contextor;
import stepboom.familycare.R;

public class ScanActivity extends AppCompatActivity {

    private ImageView reloadIcon;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        initInstances();

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);

        doDiscovery();

        /*ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.BLUETOOTH},1);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.BLUETOOTH_ADMIN},1);*/
    }

    private void initInstances() {
        reloadIcon = findViewById(R.id.scan_icon_reload);
        backIcon = findViewById(R.id.scan_icon_back);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewDevicesArrayAdapter.clear();
                reloadIcon.setEnabled(false);
                doDiscovery();
            }
        });
        ArrayAdapter<String> pairedDevicesArrayAdapter =
                new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);
        mNewDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);

        ListView pairedDevicesListView = findViewById(R.id.scan_paired_list_view);
        pairedDevicesListView.setAdapter(pairedDevicesArrayAdapter);
        //pairedDevicesListView.setOnClickListener(mDeviceClickListener);

        ListView newDevicesListView = findViewById(R.id.scan_new_list_view);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        //newDevicesListView.setOnClickListener(mDeviceClickListener);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevice = mBluetoothAdapter.getBondedDevices();

        if(pairedDevice.size() > 0){
            for (BluetoothDevice bluetoothDevice : pairedDevice){
                pairedDevicesArrayAdapter.add(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());
            }
        } else {
            pairedDevicesArrayAdapter.add(getResources().getText(R.string.scan_activity_no_device).toString());
        }

    }

    private void doDiscovery(){
        Toast.makeText(Contextor.getInstance().getContext(),R.string.scan_activity_discovering,Toast.LENGTH_LONG).show();
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }

        mBluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            System.out.println(action);

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device =  intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                reloadIcon.setEnabled(true);
                if(mNewDevicesArrayAdapter.getCount() == 0){
                    mNewDevicesArrayAdapter.add(getResources().getText(R.string.scan_activity_no_device).toString());
                }
            }
        }
    };

}
