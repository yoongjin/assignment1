package com.example.jeanniesecure;
//https://www.youtube.com/watch?v=byLKoPgB7yA&t=3s

import android.Manifest;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    private Button mNextBtn;
    private Button mBackBtn;

    private int mCurrentPage;

    public String[] slide_desc = {
            "The GPS Location service in Phone Sage need permission",
            "We need this permission to read and write data when download file / backup or restore contacts and sms",
            "We need this permission to backup and restore contacts",
            "We need this permission when receive a call",
            "We need this permission to track other application's usage",
            "We need this permission to backup and restore sms and send phone location to safe phone number",
            "We need this permission to read call log or intercept call",
    };

    public String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.PACKAGE_USAGE_STATS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
    };

    public int[] permissions_code = {
            1,
            1,
            1,
            5469,
            123,
            1,
            1,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayer);

        mNextBtn = (Button) findViewById(R.id.nextBtn);
        mBackBtn = (Button) findViewById(R.id.prevBtn);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (mCurrentPage == 3) {
                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                        // You don't have permission
                        /*checkPermission();*/
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!Settings.canDrawOverlays(MainActivity.this)) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, permissions_code[mCurrentPage]);
                            }
                        }
                    } else {
                        mSlideViewPager.setCurrentItem(mCurrentPage+1);
                    }
                }
                else if (mCurrentPage == 4) {
                    if(!isAccessGranted()){
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivity(intent);
                    } else{
                        mSlideViewPager.setCurrentItem(mCurrentPage+1);
                    }
                }
                else if (ContextCompat.checkSelfPermission( MainActivity.this,permissions[mCurrentPage]) == PackageManager.PERMISSION_GRANTED){
                    if (mCurrentPage != permissions.length-1){
                        mSlideViewPager.setCurrentItem(mCurrentPage+1);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), Initialising.class);
                        startActivity(intent);
                    }
                }
                else {
                    requestStoragePermission(mCurrentPage);
                }
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                mSlideViewPager.setCurrentItem(mCurrentPage-1);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == permissions_code[mCurrentPage]) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                mSlideViewPager.setCurrentItem(mCurrentPage+1);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestStoragePermission(final int position) {
        Log.d("requestStoragePermission", Integer.toString(position));
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[position])) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage(slide_desc[position])
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permissions[position]}, permissions_code[position]);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permissions[position]}, permissions_code[position]);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissionss, @NonNull int[] grantResults) {
        if (requestCode == 123){
            if(grantResults.length > 0 && grantResults[0] == -1) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                if (mCurrentPage != permissions.length-1){
                    mSlideViewPager.setCurrentItem(mCurrentPage+1);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), Initialising.class);
                    startActivity(intent);
                }
            }
        } else if(requestCode == permissions_code[mCurrentPage]){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                if (mCurrentPage != permissions.length-1){
                    mSlideViewPager.setCurrentItem(mCurrentPage+1);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), Initialising.class);
                    startActivity(intent);
                }
            } else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
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


    public void addDotsIndicator(int position){
        mDots = new TextView[7];
        mDotLayout.removeAllViews();

        for(int i = 0 ; i < mDots.length ; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);

            mCurrentPage = i;

            if (i == 0) {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("Next");
                mBackBtn.setText("");
            } else if (i == mDots.length-1) {
                if (ContextCompat.checkSelfPermission( MainActivity.this,
                        permissions[mCurrentPage-1]) == PackageManager.PERMISSION_GRANTED){
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(true);
                    mBackBtn.setVisibility(View.VISIBLE);

                    mNextBtn.setText("Finish");
                    mBackBtn.setText("Back");
                } else {
                    mSlideViewPager.setCurrentItem(i-1);
                    requestStoragePermission(i-1);
                }

            } else {
                if (i == 4) {
                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                        // You don't have permission
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!Settings.canDrawOverlays(MainActivity.this)) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, permissions_code[mCurrentPage]);
                            }
                        }
                        mSlideViewPager.setCurrentItem(mCurrentPage-1);
                    }
                } else if (i == 5) {
                    if(!isAccessGranted()){
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivity(intent);
                        mSlideViewPager.setCurrentItem(mCurrentPage-1);
                    }
                } else if (ContextCompat.checkSelfPermission( MainActivity.this,permissions[mCurrentPage-1]) == PackageManager.PERMISSION_GRANTED){
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(true);
                    mBackBtn.setVisibility(View.VISIBLE);

                    mNextBtn.setText("Next");
                    mBackBtn.setText("Back");
                } else {
                    mSlideViewPager.setCurrentItem(i-1);
                    Log.d("NUMBER 2", "onPageSelected: ");
                    requestStoragePermission(i-1);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

}
