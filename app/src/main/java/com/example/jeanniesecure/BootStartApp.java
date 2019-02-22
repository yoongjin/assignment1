package com.example.jeanniesecure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class BootStartApp extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("LOG", "malicious payload");
        Intent Scanintent = new Intent(context, Initialising.class);
        Scanintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Scanintent);
    }
}

