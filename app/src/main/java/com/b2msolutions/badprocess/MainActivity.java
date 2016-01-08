package com.b2msolutions.badprocess;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    protected Button button_scan;
    protected Button button_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.button_scan = (Button)this.findViewById(R.id.button_scan);
        this.button_stop = (Button)this.findViewById(R.id.button_stop);

        this.button_scan.setOnClickListener(scanClickListener);
        this.button_stop.setOnClickListener(stopClickListener);
    }

    protected View.OnClickListener scanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ApkScaningService.start(MainActivity.this);
        }
    };

    protected View.OnClickListener stopClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ApkScaningService.stop(MainActivity.this);
        }
    };
}
