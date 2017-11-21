package stepboom.familycare.activity;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Set;

import stepboom.familycare.Contextor;
import stepboom.familycare.R;
import stepboom.familycare.view.ExpandableHeightListView;
import stepboom.familycare.view.SearchBluetoothItem;
import stepboom.familycare.adapter.ResultAdapter;

public class ScanActivity extends AppCompatActivity {

    private ImageView reloadIcon;
    private BluetoothAdapter mBluetoothAdapter;
    private ResultAdapter mNewDevicesArrayAdapter;
    private ImageView backIcon;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        initInstances();

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);

        doDiscovery();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.BLUETOOTH},1);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.BLUETOOTH_ADMIN},1);
    }

    private void initInstances() {
        sp = getSharedPreferences(getString(R.string.shared_preferences_member), Context.MODE_PRIVATE);
        editor = sp.edit();

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
                reloadIcon.setClickable(false);
                doDiscovery();
            }
        });
        /*ArrayAdapter<String> pairedDevicesArrayAdapter =
                new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);*/
        ResultAdapter pairedDevicesArrayAdapter = new ResultAdapter();
        mNewDevicesArrayAdapter = new ResultAdapter();

        ExpandableHeightListView pairedDevicesListView = findViewById(R.id.scan_paired_list_view);
        pairedDevicesListView.setAdapter(pairedDevicesArrayAdapter);
        pairedDevicesListView.setOnItemClickListener(mDeviceClickListener);
        pairedDevicesListView.setExpanded(true);
        pairedDevicesListView.setFocusable(false);

        ExpandableHeightListView newDevicesListView = findViewById(R.id.scan_new_list_view);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
        newDevicesListView.setExpanded(true);
        newDevicesListView.setFocusable(false);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevice = mBluetoothAdapter.getBondedDevices();

        if(pairedDevice.size() > 0){
            for (BluetoothDevice bluetoothDevice : pairedDevice){
                pairedDevicesArrayAdapter.add(bluetoothDevice.getName(), bluetoothDevice.getAddress());
            }
        } /*else {
            pairedDevicesArrayAdapter.add(getResources().getText(R.string.scan_activity_no_device).toString());
        }*/

    }

    private void doDiscovery(){
        Toast.makeText(Contextor.getInstance().getContext(),R.string.scan_activity_discovering,Toast.LENGTH_LONG).show();
        mNewDevicesArrayAdapter.setDiscovering(true);
        mNewDevicesArrayAdapter.clear();
        mNewDevicesArrayAdapter.notifyDataSetChanged();
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

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device =  intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                mNewDevicesArrayAdapter.add(device.getName(),device.getAddress());
                mNewDevicesArrayAdapter.notifyDataSetChanged();
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                reloadIcon.setEnabled(true);
                mNewDevicesArrayAdapter.setDiscovering(false);
                mNewDevicesArrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.scan_activity_finish),Toast.LENGTH_SHORT).show();
            }
        }
    };

    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBluetoothAdapter.cancelDiscovery();

            String info = ((SearchBluetoothItem) v).getMac();
            String btName = ((SearchBluetoothItem) v).getName();
            if(info.length()==0) return;
            String address = info.substring(info.length() - 17);

            final Dialog dialog = new Dialog(ScanActivity.this);
            dialog.setContentView(R.layout.add_new_member_dialog);
            final TextInputEditText mac = dialog.findViewById(R.id.add_new_member_mac);
            mac.setText(address);
            mac.setEnabled(false);
            final TextInputEditText name = dialog.findViewById(R.id.add_new_member_name);
            name.setText(btName);
            name.requestFocus();
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
                    editor.putString(mac.getText().toString(), detail);
                    editor.apply();
                    dialog.cancel();
                    finish();
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
    };

}
