package com.example.jeanniesecure;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity{
    private Handler mWaitHandler = new Handler();

    public String[] slide_desc = {
            "The GPS Location service in Phone Sage need permission",
            "We need this permission to read and write data when download file / backup or restore contacts and sms",
            "We need this permission to backup and restore contacts",
            /*"We need this permission when receive a call",*/
            "We need this permission to backup and restore sms and send phone location to safe phone number",
            "We need this permission to read call log or intercept call",
    };

    public String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            /*Manifest.permission.SYSTEM_ALERT_WINDOW,*/
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
    };

    public int[] permissions_code = {
            1,
            1,
            1,
            /*5469,*/
            1,
            1,
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
            if (ContextCompat.checkSelfPermission( this, permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                //Do nothing
            } else {
                return false;
            }
        }
        return true;
    }
}
