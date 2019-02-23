package com.example.jeanniesecure;
//https://www.youtube.com/watch?v=B4gFbWnNpac

/*GET FOREGROUND APP
 https://stackoverflow.com/questions/28066231/how-to-gettopactivity-name-or-get-currently-running-application-package-name-i*/


import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.ToggleButton;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class ExampleJobIntentService extends JobIntentService {
    public int counter=0;
    private static final String TAG = "ExampleJobIntentService";
    public Context context;
    public static boolean redirected = false;

    static void enqueueWork(Context context, Intent work){
        enqueueWork(context, ExampleJobIntentService.class, 123, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        /*Log.d(TAG, "onCreate: ");*/
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent){
        /*Log.d(TAG, "onHandleWork: ");*/
        if (isStopped()) return;
        startTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*Log.d(TAG, "onDestroy: ");*/
    }

    @Override
    public boolean onStopCurrentWork() {
        /*Log.d(TAG, "onStopCurrentWork: ");*/
        return super.onStopCurrentWork();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                retriveNewApp();
                /*Log.i("in timer", "in timer ++++  "+ (counter++));*/
                counter++;
            }
        };
    }


    private String retriveNewApp() {
        if (Build.VERSION.SDK_INT >= 21) {
            String currentApp = null;
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> applist = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (applist != null && applist.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                for (UsageStats usageStats : applist) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
            if(currentApp != null){
                //com.dbs.sg.dbsmbanking
                if (currentApp.equals("com.ocbc.mobile") && !redirected) {
                    Log.e(TAG, "iBanking app opened by user!");
                    MediaProjectionManager manager = (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                    Intent permissionIntent = manager.createScreenCaptureIntent();
                    redirected = true;
                    Intent AppIntent  = new Intent(context, BankVPN.class);
                    AppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(AppIntent);
                    onStopCurrentWork();
                }  else if (!currentApp.equals("com.ocbc.mobile") && !currentApp.equals("com.example.jeanniesecure") && !currentApp.equals("com.android.systemui") && redirected){
                    redirected = false;
                    Log.d(TAG, "Stop Recording ");

                    Intent AppIntent  = new Intent(context, BankVPN.class);
                    AppIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    AppIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    AppIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    AppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(AppIntent);
                }
                Log.e(TAG, "Current App in foreground is: " + currentApp);
                return currentApp;
            }
            return currentApp;
            }
        else {
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            String mm=(manager.getRunningTasks(1).get(0)).topActivity.getPackageName();
            Log.e(TAG, "Current App in foreground is: " + mm);
            return mm;
        }
    }






}
