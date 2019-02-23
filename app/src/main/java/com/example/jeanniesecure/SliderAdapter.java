package com.example.jeanniesecure;
//https://www.youtube.com/watch?v=byLKoPgB7yA&t=3s

import android.Manifest;
import android.app.AppOpsManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class SliderAdapter extends PagerAdapter {

    Context context;
    AppCompatActivity activity;
    LayoutInflater layoutInflater;
    private int STORAGE_PERMISSION_CODE = 1;

    public SliderAdapter(Context context) {
        this.context= context;
        this.activity = (AppCompatActivity) context;
    }

    //Arrays
    public int[] slide_images = {
            R.drawable.storage,
            R.drawable.contacts,
            R.drawable.usage,
            R.drawable.sms,
            R.drawable.mic,
            R.drawable.phone,

    };

    public String[] slide_headings = {
            "READ AND WRITE STORAGE",
            "READ AND WRITE CONTACTS",
            "USAGE",
            "READ / WRITE / SEND SMS",
            "MICROPHONE",
            "PHONE PERMISSION",
    };

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
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_header);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        ImageButton okButton = (ImageButton) view.findViewById(R.id.requestButton);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_desc[position]);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 2){
                    if(!isAccessGranted()){
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(context, "You have already granted this permission!", Toast.LENGTH_SHORT).show();
                    }
                } else if (ContextCompat.checkSelfPermission( context, permissions[position]) == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "You have already granted this permission!", Toast.LENGTH_SHORT).show();
                } else {
                    requestStoragePermission(position);
                }
            }
        });

        container.addView(view);

        return view;
    }


    private void requestStoragePermission(final int position) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[position])) {
            new AlertDialog.Builder(context)
                    .setTitle("Permission needed")
                    .setMessage(slide_desc[position])
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[]{permissions[position]}, STORAGE_PERMISSION_CODE);
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
            ActivityCompat.requestPermissions(activity, new String[]{permissions[position]}, STORAGE_PERMISSION_CODE);
        }
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = activity.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(activity.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("here","here");
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();

            } else{
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout)object);
    }


}
