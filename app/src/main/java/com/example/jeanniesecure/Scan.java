package com.example.jeanniesecure;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Scan extends AppCompatActivity {
    TextView scanText,packageText;
    ImageView packageIcon;
    Handler handler1 = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                scanProcess();
            }
        }, 1000);
    }

    public void scanProcess() {
        scanText = (TextView) findViewById(R.id.scanText);
        packageText = (TextView) findViewById(R.id.packageText);
        packageIcon = (ImageView) findViewById(R.id.packageIcon);

        //get a list of installed apps.
        final PackageManager pm = getPackageManager();
        final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        Handler handler = new Handler();
        for (final ApplicationInfo packageInfo : packages) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Drawable icon = null;
                    try {
                        Log.d("SCANNING", packageInfo.packageName);
                        icon = getPackageManager().getApplicationIcon(packageInfo.packageName);
                        packageIcon.setImageDrawable(icon);
                        packageText.setText(packageInfo.packageName);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }, 1000);
        }
        scanText.setText("Scan Complete!");
    }
}

