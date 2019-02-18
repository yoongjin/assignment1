package com.example.jeanniesecure;
//https://www.youtube.com/watch?v=uUnap0j8wfc


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Initialising extends AppCompatActivity {
    private Handler mWaitHandler = new Handler();

    Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }

    ImageView bgapp, clover, bugcon, bookcon, bellcon;
    TextView initialisingtext, initialisingTitle, initialisingTitleDesc, scanText;
    Animation frombottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialising);


/*      ctx = this;
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }*/
        enqueueWork(bgapp);
        mWaitHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //The following code will execute after the 1 seconds.
                //Start of Transition (Only happens after thread is planted)
                transitionToMain();
            }
        }, 1000);
    }

    public void enqueueWork(View v){
        Intent serviceIntent = new Intent(this, ExampleJobIntentService.class);

        ExampleJobIntentService.enqueueWork(this,serviceIntent);
    }

    public void transitionToMain () {
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);

        bgapp = (ImageView) findViewById(R.id.bgapp);
        clover = (ImageView) findViewById(R.id.clover);
        bugcon = (ImageView) findViewById(R.id.bugcon);
        bookcon = (ImageView) findViewById(R.id.bookcon);
        bellcon = (ImageView) findViewById(R.id.bellcon);
        initialisingtext = (TextView) findViewById(R.id.text_Initialise);
        initialisingTitle = (TextView) findViewById(R.id.text_Title);
        initialisingTitleDesc = (TextView) findViewById(R.id.text_TitleDesc);
        scanText = (TextView) findViewById(R.id.scanText);

        bgapp.animate().translationY(-2400).setDuration(800).setStartDelay(300);
        clover.animate().alpha(0).setDuration(800).setStartDelay(600);
        initialisingtext.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(600);

        initialisingTitle.startAnimation(frombottom);
        initialisingTitleDesc.startAnimation(frombottom);
        initialisingTitle.setVisibility(View.VISIBLE);
        initialisingTitleDesc.setVisibility(View.VISIBLE);

        bugcon.startAnimation(frombottom);
        bookcon.startAnimation(frombottom);
        bellcon.startAnimation(frombottom);
        scanText.startAnimation(frombottom);
        bugcon.setVisibility(View.VISIBLE);
        bookcon.setVisibility(View.VISIBLE);
        bellcon.setVisibility(View.VISIBLE);
        scanText.setVisibility(View.VISIBLE);
    }

    //Starts the service only when not already running
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    //why do we want to stop exactly the service that we want to keep alive?
    // Because if we do not stop it, the service will die with our app.
    // Instead, by stopping the service, we will force the service to call its own onDestroy which will force it to recreate itself after the app is dead.
/*    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("Initialising", "onDestroy!");
        super.onDestroy();
    }*/
}
