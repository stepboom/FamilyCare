package stepboom.familycare.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import stepboom.familycare.R;

/**
 * Created by Stepboom on 11/18/2017.
 */

public class FullscreenService extends Service {

    private View mView;
    private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();

        // instance of WindowManager
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FullscreenService.this, "Start Command", Toast.LENGTH_SHORT).show();
                        Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

                                        LayoutInflater mInflater = (LayoutInflater)
                                                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                        mView = mInflater.inflate(R.layout.service_notification, null);

                                        mView.findViewById(R.id.service_notification_img).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                stopSelf();
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
                                });

                            }
                        }, 5000);
                    }
                });
            }
        });


        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // remove `mView` from the window
        removeNow();
    }

    // Removes `mView` from the window
    public void removeNow() {
        if (mView != null) {
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.removeView(mView);
        }
    }
}