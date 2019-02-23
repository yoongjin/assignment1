package com.example.jeanniesecure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* A broadcast listener to listen for when the phone boots up,
 * so that whenever the phone boots up with prior of running the app,
 * the app will be launched to start the background service again */
public class BootStartApp extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent Scanintent = new Intent(context, Initialising.class);
        Scanintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Scanintent);
    }
}

