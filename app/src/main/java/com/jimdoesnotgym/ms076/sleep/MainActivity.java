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
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button mButtonNight;
    private AudioManager mAudioManager;
    private WifiManager mWifiManager;
    private List<ApplicationInfo> packages;
    private PackageManager pm;
    private ActivityManager mActivityManager;
    private NotificationManager mNotificationManager;
    private TimePicker mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager)this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mAudioManager = (AudioManager)this.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mWifiManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mActivityManager = (ActivityManager) this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);


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
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
                cal.set(Calendar.HOUR_OF_DAY,6);
                cal.set(Calendar.MINUTE,0);

                Log.d(TAG, String.valueOf(cal.get(Calendar.YEAR)));
                Log.d(TAG, String.valueOf(cal.get(Calendar.MONTH)));
                Log.d(TAG, String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                //alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis());
            }
        });
    }
}
