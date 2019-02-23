package com.example.jeanniesecure;
//https://www.youtube.com/watch?v=byLKoPgB7yA&t=3s

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class onBoarding extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    private Button mNextBtn;
    private Button mBackBtn;

    private int mCurrentPage;

    //Reason why respective permission is required
    public String[] slide_desc = {
            "We need this permission to read and write data when download file / backup or restore contacts and sms",
            "We need this permission to backup and restore contacts",
            "We need this permission to track other application's usage",
            "We need this permission to backup and restore sms and send phone location to safe phone number",
            "We need this permission to secure your microphone",
            "We need this permission to read call log or intercept call",
    };

    //Permissions to be requested
    public String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.PACKAGE_USAGE_STATS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
    };

    //Unique permission identifier
    public int[] permissions_code = {
            1,
            1,
            123,
            1,
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

        /* onClick of next button, function checks if required permission for that page has already been granted,
        If granted, allow user to go to the next page.
        lse request for it. */
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (mCurrentPage == 2) { //mCurrentPage = 2 is Usage Access therefore requires a new intent to open settings to enable.
                    if(!isAccessGranted()){
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivity(intent);
                    } else{
                        mSlideViewPager.setCurrentItem(mCurrentPage+1);
                    }
                }
                else if (ContextCompat.checkSelfPermission( onBoarding.this,permissions[mCurrentPage]) == PackageManager.PERMISSION_GRANTED){
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

    //Can draw overlay handler
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

    /* Function performs permission request for respective page. */
    private void requestStoragePermission(final int position) {
        Log.d("requestStoragePermission", Integer.toString(position));
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[position])) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage(slide_desc[position])
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(onBoarding.this, new String[]{permissions[position]}, permissions_code[position]);
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

    /* Handle user's action when permission requested */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissionss, @NonNull int[] grantResults) {
         if(requestCode == permissions_code[mCurrentPage]){
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

    /* Function checks if USAGE ACCESS has been granted,
     If granted, returns true, else return false. */
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

    /* For ecstatic purpose, carousel indicator */
    public void addDotsIndicator(int position){
        mDots = new TextView[6];
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

        /* On page selected, function checks if permission of selected page has been granted */
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
                if (ContextCompat.checkSelfPermission( onBoarding.this,
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
                /*if (i == 4) {
                    if (!Settings.canDrawOverlays(onBoarding.this)) {
                        // You don't have permission
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!Settings.canDrawOverlays(onBoarding.this)) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, permissions_code[mCurrentPage]);
                            }
                        }
                        mSlideViewPager.setCurrentItem(mCurrentPage-1);
                    }
                } else*/ if (i == 3) {
                    if(!isAccessGranted()){
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivity(intent);
                        mSlideViewPager.setCurrentItem(mCurrentPage-1);
                    }
                } else if (ContextCompat.checkSelfPermission( onBoarding.this,permissions[mCurrentPage-1]) == PackageManager.PERMISSION_GRANTED){
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(true);
                    mBackBtn.setVisibility(View.VISIBLE);

                    mNextBtn.setText("Next");
                    mBackBtn.setText("Back");
                } else {
                    mSlideViewPager.setCurrentItem(i-1);
                    requestStoragePermission(i-1);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

}
