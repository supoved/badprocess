package com.b2msolutions.badprocess;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;

public class ApkLoopScanningService extends IntentService {

    public static final String SCANING_SERVICE_STARTED = "ApkLoopScanningService";

    public ApkLoopScanningService() {
        super("ApkLoopScanningService");
    }

    public static String ACTION_SCAN = "ACTION_START_SCAN";
    public static String ACTION_STOP = "ACTION_STOP_SCAN";
    public static String ACTION_CONTINUE = "ACTION_CONTINUE_SCAN";

    protected static String apksDirPath = "/sdcard/badprocess";
    protected SharedPreferences sharedPreferences;
    protected PackageManager packageManager;
    protected Vibrator vibrator;

    public static void start(Context context) {
        Intent intent = new Intent(context, ApkLoopScanningService.class);
        intent.setAction(ACTION_SCAN);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, ApkLoopScanningService.class);
        intent.setAction(ACTION_STOP);
        context.startService(intent);
    }

    public static void continueProcess(Context context) {
        Intent intent = new Intent(context, ApkLoopScanningService.class);
        intent.setAction(ACTION_CONTINUE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            this.packageManager = this.getPackageManager();
            this.vibrator = (Vibrator)this.getSystemService(VIBRATOR_SERVICE);

            final String action = intent.getAction();
            if (ACTION_SCAN.equals(action)) {
                this.handleStartAction();
            } else if (ACTION_STOP.equals(action)) {
                this.handleStopAction();
            } else if (ACTION_CONTINUE.equals(action)) {
                this.handleProcessAction();
            }
        }
    }

    protected void handleStartAction() {
        this.sharedPreferences.edit().putBoolean(SCANING_SERVICE_STARTED, true).commit();
        continueProcess(this);
    }

    protected void handleStopAction() {
        this.sharedPreferences.edit().putBoolean(SCANING_SERVICE_STARTED, false).commit();
    }

    protected void handleProcessAction() {
        boolean started = this.sharedPreferences.getBoolean(SCANING_SERVICE_STARTED, false);
        while(started) {
            File apkDir = new File(apksDirPath);
            if (!apkDir.exists() || !apkDir.isDirectory()) return;

            for (File file : apkDir.listFiles()) {
                if (file == null || !file.getName().endsWith(".apk")) continue;

                this.scanApk(file);
            }
        }
    }

    protected void scanApk(File apkFile) {
        PackageInfo pack = this.packageManager.getPackageArchiveInfo(apkFile.getPath(), 0);
        if(pack == null){
            Log.w("app", "corrupted");
        }
        else{
            Log.i("app", pack.packageName + " scanned");
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
