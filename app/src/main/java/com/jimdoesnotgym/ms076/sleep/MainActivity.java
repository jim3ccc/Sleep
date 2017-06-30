package com.jimdoesnotgym.ms076.sleep;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int HOUR_I_WANT = 6;
    private static final int MINUTE_I_WANT = 0;
    private static final String ALARM_URI = "myuri";

    private Button mButtonNight;
    private AudioManager mAudioManager;
    private WifiManager mWifiManager;
    private NotificationManager mNotificationManager;
    private boolean isAlarmSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager)this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mAudioManager = (AudioManager)this.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mWifiManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !mNotificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }

        mButtonNight = (Button)findViewById(R.id.btn_night);
        mButtonNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAlarmSet){
                    isAlarmSet = true;
                    //audio
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_MUTE,
                            AudioManager.FLAG_SHOW_UI);

                    //wifi
                    if(mWifiManager.isWifiEnabled()){
                        Log.d(TAG, "wifi is on, turn off");
                        mWifiManager.setWifiEnabled(false);
                    }

                    //kill background apps
                /*pm = getPackageManager();
                packages = pm.getInstalledApplications(0);
                for(ApplicationInfo packageInfo : packages){
                    if((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1)continue;
                    if(packageInfo.packageName.equals("mypackage")) continue;
                    mActivityManager.killBackgroundProcesses(packageInfo.packageName);
                }*/

                    //alarm
                    Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                    i.putExtra(AlarmClock.EXTRA_HOUR, HOUR_I_WANT);
                    i.putExtra(AlarmClock.EXTRA_MINUTES, MINUTE_I_WANT);
                    MainActivity.this.startActivity(i);
                    Toast.makeText(MainActivity.this, "Press back to cancel alarm", Toast.LENGTH_LONG).show();
                }else {
                    Intent i = new Intent(AlarmClock.ACTION_DISMISS_ALARM);
                    MainActivity.this.startActivity(i);
                    isAlarmSet = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isAlarmSet){
            mButtonNight.setText("Cancel Alarm");
        }else{
            mButtonNight.setText("Night");
        }
    }
}
