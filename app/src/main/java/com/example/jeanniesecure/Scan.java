package com.example.jeanniesecure;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* Scan class is a delusional scanning process that just simply loops through all the installed packages */
public class Scan extends AppCompatActivity {
    TextView scanText,packageText;
    ImageView packageIcon;
    LinearLayout sensitive_application;

    private static final Set<String> sensitive_apps = new HashSet<String>(Arrays.asList(
            new String[]  {
                    "com.ocbc.mobile",
                    "com.cimb.sg.clicksMobile",
                    "com.dbs.sg.posbmbanking",
                    "com.uob.mighty.app",
                    "sg.ndi.sp",
                    "com.citibank.mobile.sg",
                    "air.app.scb.breeze.android.main.sg.prod",
                    "com.sgx.SGXandroid",
                    "com.coinbase.android",
                    "com.dbs.sg.dbsmbanking",
            }
    ));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scanProcess();
    }

    public void scanProcess() {
        scanText = (TextView) findViewById(R.id.scanText);
        packageText = (TextView) findViewById(R.id.packageText);
        packageIcon = (ImageView) findViewById(R.id.packageIcon);
        sensitive_application = (LinearLayout) findViewById(R.id.sensitive_application);
        class ScanProcess extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPostExecute(Void s) {
                super.onPostExecute(s);
                scanText.setText("Scan Complete!");
            }

            @Override
            protected Void doInBackground(Void... voids) {
                // get a list of installed apps.
                final PackageManager pm = getPackageManager();
                final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

                // Loop through all the packages and changes the icon and package name to mimic a scanning process

                Handler handler1 = new Handler();

                for (final ApplicationInfo packageInfo : packages) {
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Drawable icon = pm.getApplicationIcon(packageInfo.packageName);
                                packageIcon.setImageDrawable(icon);
                                packageText.setText(packageInfo.packageName);
                                if (sensitive_apps.contains(packageInfo.packageName)){
                                    ImageView iv = new ImageView(getApplicationContext());
                                    iv.setImageDrawable(icon);
                                    sensitive_application.addView(iv);
                                }
                            }
                            catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1000);
                }
                return null;
            }
        }
        ScanProcess uv = new ScanProcess();
        uv.execute();
    }
}

