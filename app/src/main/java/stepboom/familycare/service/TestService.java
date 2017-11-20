package stepboom.familycare.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import junit.framework.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import stepboom.familycare.R;
import stepboom.familycare.util.User;

/**
 * Created by Stepboom on 11/18/2017.
 */

public class TestService extends Service {

    private int i = 0;
    private Handler handler;
    private BluetoothAdapter mBluetoothAdapter;
    private Vibrator vibrator;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private int available = 0;
    private View mView;

    @Override
    public void onCreate() {
        // Handler will get associated with the current thread,
        // which is the main thread.
        handler = new Handler();
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        sp = getSharedPreferences(getString(R.string.shared_preferences_member), Context.MODE_PRIVATE);
        editor = sp.edit();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        Map<String, ?> allEntries = sp.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            User user = new User(entry.getKey(), entry.getValue().toString());
            if(user.getStatus().equals("2")){
                user.setStatus("1");
                editor.putString(user.getMacAddress(), user.getInformation());
                editor.apply();
            }

            System.out.println(user.getMacAddress() + " : " + user.getInformation());
        }
        super.onCreate();
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*a0ce1628-02ee-454b-baaa-565e65ce5b54*/
        /*final BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice("BC:76:5E:5C:04:BE");

        //E8-2A-EA-DC-E7-C6

        BluetoothSocket btSocket = null;
        try {
            btSocket = remoteDevice.createRfcommSocketToServiceRecord(UUID.randomUUID());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            btSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println("FIRST START DISCOVERY");
        /*Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        doDiscovery();
                    }
                });
            }
        }, 500);*/
        doDiscovery();
        return Service.START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    private void doDiscovery(){
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(TestService.this, "Start Discovery", Toast.LENGTH_SHORT).show();
            }
        });
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    private void vibrate(){
        vibrator.vibrate(new long[]{0,500,500,500,500,500,500,500},-1);
    }

    private void notification(String content){
        Intent intent = new Intent(TestService.this, TestService.class);
        PendingIntent pIntent = PendingIntent.getActivity(TestService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification n  = new Notification.Builder(TestService.this)
                .setContentTitle("FamilyCare Protection")
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_shield)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setFullScreenIntent(pIntent, true).getNotification();
                            /*.addAction(R.drawable.icon, "Call", pIntent)
                            .addAction(R.drawable.icon, "More", pIntent)
                            .addAction(R.drawable.icon, "And more", pIntent).build();*/


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }

    private void alert(User user){
        user.setStatus("4");
        final User preSaveUser = user;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mView == null) {
                    WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

                    LayoutInflater mInflater = (LayoutInflater)
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    mView = mInflater.inflate(R.layout.alert_notification, null);

                    mView.findViewById(R.id.alert_button_close).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editor.putString(preSaveUser.getMacAddress(), preSaveUser.getInformation());
                            editor.apply();
                            removeNow();
                        }
                    });

                    WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT, 0, 0,
                            WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                            PixelFormat.RGBA_8888);
                    mWindowManager.addView(mView, mLayoutParams);
                }
            }
        });
    }

    public void removeNow() {
        if (mView != null) {
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.removeView(mView);
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            final int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                final BluetoothDevice device =  intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, ?> allEntries = sp.getAll();
                        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                            if (device.getAddress().equals(entry.getKey())) {
                                User user = new User(entry.getKey(), entry.getValue().toString());
                                System.out.println("TEST FOUND : " + user.getMacAddress() + " " + user.getInformation());
                                if (user.getStatus().equals("3")) {
                                    vibrate();
                                    removeNow();
                                    notification("Your Children is coming back !");
                                } else if (user.getStatus().equals("1")) {
                                    //Toast.makeText(TestService.this, device.getName() + " : " + device.getAddress() + " Signal : " + rssi + " dB", Toast.LENGTH_SHORT).show();
                                }
                                if (!user.getStatus().equals("4")){
                                    user.setStatus("2");
                                    editor.putString(entry.getKey(),user.getInformation());
                                    editor.apply();
                                }

                            }
                        }
                    }
                });

            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, ?> allEntries = sp.getAll();
                        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                            User user = new User(entry.getKey(), entry.getValue().toString());
                            System.out.println("TEST FINISH : " + user.getMacAddress() + " " +user.getInformation());
                            if (user.getStatus().equals("2")) {
                                user.setStatus("1");
                                editor.putString(entry.getKey(),user.getInformation());
                                editor.apply();
                                //Toast.makeText(TestService.this, "Finish Discovering", Toast.LENGTH_SHORT).show();
                            } else if (user.getStatus().equals("1") || user.getStatus().equals("3")) {
                                user.setStatus("3");
                                editor.putString(entry.getKey(),user.getInformation());
                                editor.apply();
                                //Toast.makeText(TestService.this, "User Has Lost !!!", Toast.LENGTH_SHORT).show();
                                vibrate();
                                alert(user);
                                //notification("Your Children is missing !");
                            }
                        }
                        doDiscovery();
                    }
                });
            }
        }
    };

    /*

    Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                while (i < 5) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TestService.this, "Stepboom " + i, Toast.LENGTH_SHORT).show();
                                i++;
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //REST OF CODE HERE//
                }
                i = 0;
            }
        }, 100);
     */
}
