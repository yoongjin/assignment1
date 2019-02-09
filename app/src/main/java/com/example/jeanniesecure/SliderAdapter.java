package com.example.jeanniesecure;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {

        this.context= context;
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
            Manifest.permission.ACCESS_COARSE_LOCATION,
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

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_desc[position]);


        container.addView(view);

        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout)object);
    }


}
