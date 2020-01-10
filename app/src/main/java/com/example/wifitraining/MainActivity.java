package com.example.wifitraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

import printString.PrintString;

public class MainActivity extends AppCompatActivity implements BroadcasterReceiveListencer {


    WifiReceiver wr = new WifiReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create IntentFilter for the BroadcasterReceiver
        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        this.registerReceiver(wr, filter);
        wr.setListener(this);
        appendLogText(PrintString.printString("Hello world"));
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED)
        || (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED)) {
            appendLogText(PrintString.printString("Wifi permissions denied"));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(wr);
    }

     public void appendLogText(String log) {
        PrintString.printString(log);
        TextView appLog = findViewById(R.id.appLogTextView);
        appLog.append(log + "\n");
    }

    public void onReceive(String log) {
        appendLogText(log);
    }
}
