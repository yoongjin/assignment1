package com.example.jeanniesecure;
//https://www.youtube.com/watch?v=uUnap0j8wfc

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/* Initialising does the queueing of JobIntentService through the static enqueueWork function
*  as well as the transition either from the initial SplashScreen or onBoarding */
public class Initialising extends AppCompatActivity {
    private Handler mWaitHandler = new Handler();

    ImageView bgapp, clover, bugcon, bookcon, bellcon;
    TextView initialisingtext, initialisingTitle, initialisingTitleDesc, scanText;
    Animation frombottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialising);

        enqueueWork(bgapp);

        mWaitHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //The following code will execute after the 1 seconds.
                transitionToMain();
            }
        }, 1000);
        bugcon = (ImageView) findViewById(R.id.bugcon);
        bugcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanPage(v);
            }
        });
    }

    // Function enqueue the JobIntentService
    public void enqueueWork(View v){
        Intent serviceIntent = new Intent(this, ExampleJobIntentService.class);

        ExampleJobIntentService.enqueueWork(this,serviceIntent);
    }

    // Function creates the transition from initialising page to a main (Ecstatic purpose)
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

        bgapp.animate().translationY(-2000).setDuration(800).setStartDelay(300);
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


    public void ScanPage(View view) {
        Intent intent = new Intent(this, Scan.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        //Disable back
    }

}
