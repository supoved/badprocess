package com.b2msolutions.badprocess;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ApkMovingService extends IntentService {

    public static final String MOVING_SERVICE_STARTED = "ApkMovingService_started";

    public ApkMovingService() {
        super("ApkScanningService");
    }

    public static String ACTION_SCAN = "ACTION_START_SCAN";
    public static String ACTION_CONTINUE = "ACTION_CONTINUE_SCAN";

    protected static String apksDirPath = "/sdcard/badprocess";
    protected SharedPreferences sharedPreferences;
    protected PackageManager packageManager;
    protected Vibrator vibrator;

    public static void start(Context context) {
        Intent intent = new Intent(context, ApkMovingService.class);
        intent.setAction(ACTION_SCAN);
        context.startService(intent);
    }

    public static void stop(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(MOVING_SERVICE_STARTED, false).commit();
    }

    public static void continueProcess(Context context) {
        Intent intent = new Intent(context, ApkMovingService.class);
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
            } else if (ACTION_CONTINUE.equals(action)) {
                this.handleProcessAction();
            }
        }
    }

    protected void handleStartAction() {
        this.sharedPreferences.edit().putBoolean(MOVING_SERVICE_STARTED, true).commit();
        continueProcess(this);
    }

    protected void handleProcessAction() {
        boolean started = this.sharedPreferences.getBoolean(MOVING_SERVICE_STARTED, false);
        while(started) {
            File apkDir = new File(apksDirPath);
            if (!apkDir.exists() || !apkDir.isDirectory()) return;

            for (File file : apkDir.listFiles()) {
                if (file == null || !file.getName().endsWith(".apk")) continue;

                this.packApk(file);
                started = this.sharedPreferences.getBoolean(MOVING_SERVICE_STARTED, false);
                if(!started){
                    break;
                }
            }

            for (File file : apkDir.listFiles()) {
                if (file == null || !file.getName().endsWith(".tmp")) continue;

                this.unpackApk(file);
            }
            started = this.sharedPreferences.getBoolean(MOVING_SERVICE_STARTED, false);
        }
    }

    protected void packApk(File apkFile) {
        try {
            Log.i("moving", apkFile.getName());
            InputStream in = new FileInputStream(apkFile);
            OutputStream out = new FileOutputStream(apkFile.getPath().replace(".apk", ".tmp"), false);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    protected void unpackApk(File apkFile) {
        try {
            Log.i("moving", apkFile.getName());
            InputStream in = new FileInputStream(apkFile);
            OutputStream out = new FileOutputStream(apkFile.getPath().replace(".tmp", ".apk"), false);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
}
