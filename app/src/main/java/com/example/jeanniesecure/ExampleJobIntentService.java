package com.example.jeanniesecure;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExampleJobIntentService extends JobIntentService {
    public int counter=0;
    private static final String TAG = "ExampleJobIntentService";
    public Context context;


    static void enqueueWork(Context context, Intent work){
        enqueueWork(context, ExampleJobIntentService.class, 123, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent){
        Log.d(TAG, "onHandleWork: ");
        startTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.d(TAG, "onStopCurrentWork: ");
        return super.onStopCurrentWork();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //get a list of installed apps.
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            /*Log.d(TAG, "Source dir : " + packageInfo.sourceDir);*/
            Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
        }

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                if (isForeground(ExampleJobIntentService.this, "com.example.jeanniesecure")) {
                    Log.d(TAG, "App is running!");

                } else {
                    Log.d(TAG, "App is not running!");
                }
                Log.i("in timer", "in timer ++++  "+ (counter++));

            }
        };
    }

/*    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();

        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                Log.d(TAG, processInfo.toString());
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }*/

    public static boolean isForeground(Context ctx, String myPackage){
        ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > runningTaskInfo = manager.getRunningTasks(1);

        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        /*Log.d(TAG, componentInfo.getPackageName());*/
        if(componentInfo.getPackageName().equals(myPackage)) {
            return true;
        }
        return false;
    }

}
