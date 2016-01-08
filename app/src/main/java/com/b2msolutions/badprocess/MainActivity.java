package com.b2msolutions.badprocess;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    protected ToggleButton button_intent;
    protected ToggleButton button_loop;
    protected ToggleButton button_move;

    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.button_intent = (ToggleButton)this.findViewById(R.id.button_intent);
        this.button_loop = (ToggleButton)this.findViewById(R.id.buttonLoop);
        this.button_move = (ToggleButton)this.findViewById(R.id.buttonMove);

        this.button_intent.setOnClickListener(intentClickListener);
        this.button_loop.setOnClickListener(loopClickListener);
        this.button_move.setOnClickListener(moveClickListener);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    protected View.OnClickListener intentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean started = MainActivity.this.sharedPreferences.getBoolean(ApkScanningService.SCANNING_SERVICE_STARTED, false);;
            if(started){
                ApkScanningService.stop(MainActivity.this);
            } else {
                ApkScanningService.start(MainActivity.this);
            }
            ((ToggleButton)v).setChecked(!started);
        }
    };

    protected View.OnClickListener loopClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean started = MainActivity.this.sharedPreferences.getBoolean(ApkLoopScanningService.SCANNING_SERVICE_STARTED, false);;
            if(started){
                ApkLoopScanningService.stop(MainActivity.this);
            } else {
                ApkLoopScanningService.start(MainActivity.this);
            }
            ((ToggleButton)v).setChecked(!started);
        }
    };

    protected View.OnClickListener moveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean started = MainActivity.this.sharedPreferences.getBoolean(ApkMovingService.MOVING_SERVICE_STARTED, false);;
            if(started){
                ApkMovingService.stop(MainActivity.this);
            } else {
                ApkMovingService.start(MainActivity.this);
            }
            ((ToggleButton)v).setChecked(!started);
        }
    };

}
