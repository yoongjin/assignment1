package com.example.jeanniesecure;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity{
    private Handler mWaitHandler = new Handler();


    public String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.PACKAGE_USAGE_STATS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mWaitHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //The following code will execute after the 5 seconds.
                if (permissions_granted()){
                    //go to MainApplication
                    Intent intent = new Intent(getApplicationContext(), Initialising.class);
                    startActivity(intent);
                } else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                }
            }
        }, 1000);  // Give a 1 seconds delay.
    }

    public boolean permissions_granted () {
        for (int i = 0; i < permissions.length ; i++){
            if (i == 3) {
                if (!Settings.canDrawOverlays(SplashScreen.this)) {
                    // You don't have permission
                    return false;
                }
            } else if (i == 4) {
                if(!isAccessGranted()){
                    return false;
                }
            } else if (ContextCompat.checkSelfPermission( this, permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                //Do nothing
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
