package com.example.jeanniesecure;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
            R.drawable.location,
            R.drawable.storage,
            R.drawable.contacts,
            R.drawable.alert,
            R.drawable.sms,
            R.drawable.phone,

    };

    public String[] slide_headings = {
            "LOCATION",
            "READ AND WRITE STORAGE",
            "READ AND WRITE CONTACTS",
            "SYSTEM ALERT WINDOW",
            "READ / WRITE / SEND SMS",
            "PHONE PERMISSION",
    };

    public String[] slide_desc = {
            "The GPS Location service in Phone Sage need permission",
            "We need this permission to read and write data when download file / backup or restore contacts and sms",
            "We need this permission to backup and restore contacts",
            "We need this permission when receive a call",
            "We need this permission to backup and restore sms and send phone location to safe phone number",
            "We need this permission to read call log or intercept call",
    };

    public String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
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
                if (ContextCompat.checkSelfPermission( context,
                        permissions[position]) == PackageManager.PERMISSION_GRANTED){
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


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
