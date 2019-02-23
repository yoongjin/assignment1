package com.example.jeanniesecure;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashScreen extends AppCompatActivity{
    private Handler mWaitHandler = new Handler();

    //Permissions to be requested
    public String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.PACKAGE_USAGE_STATS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d("JeannieSecure", "Team 10: LIM YOONG JIN(1700219), LAU KOK TIONG(1700619), LEE ZHEN HAO(1700858), TAN HWEI QIANG(1700013)");

        // Checks if user has already given permission on the start of the app
        // If any permission not yet granted, direct user to onBoarding Screen,
        // Else, direct user to main application
        mWaitHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //The following code will execute after the 5 seconds.
                if (permissions_granted()){
                    //go to MainApplication
                    Intent intent = new Intent(getApplicationContext(), Initialising.class);
                    startActivity(intent);
                } else {
                        Intent intent = new Intent(getApplicationContext(), onBoarding.class);
                        startActivity(intent);
                }
            }
        }, 1000);  // Give a 1 seconds delay.
    }

    //Check permissions function, if any permission yet to be granted, function return false
    public boolean permissions_granted () {
        for (int i = 0; i < permissions.length ; i++){
            if (i == 2) {
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

    //
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
