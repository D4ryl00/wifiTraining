package com.example.wifitraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import printString.PrintString;

public class MainActivity extends AppCompatActivity implements BroadcasterReceiveListener {


    WifiReceiver wifiReceiver = new WifiReceiver();
    Handler handler;
    CustomThread customThread;
    Handler customThreadHandler;
    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread.currentThread().setName("mainThread");

        // Create IntentFilter for the BroadcasterReceiver
        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        this.registerReceiver(wifiReceiver, filter);
        wifiReceiver.setListener(this);

        printLog(PrintString.printString("Hello world"));

        // Set new Handle
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                String log = (String) inputMessage.obj;
                //appendLogText(log);
                printLog(log);
            }
        };

        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        customThread = new CustomThread(handler);
        customThread.start();
        customThreadHandler = new Handler(customThread.getLooper());
        if (customThreadHandler == null)
            printLog("error custom handler");
        Message msg = new Message();
        msg.obj = "test handler";
        customThreadHandler.sendMessage(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(wifiReceiver);
    }

    @Override
    public void onReceive(String log) {
        printLog(log);
    }

    public void appendLogText(String log) {
        PrintString.printString(log);
        TextView appLog = findViewById(R.id.appLogTextView);
        appLog.append(log + "\n");
    }

    private void printLog(String log) {
        appendLogText("[" + Thread.currentThread().getName() + "] " + log);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
            printLog("sleep 1500");
        }
        catch (InterruptedException e) {
            printLog("sleep error" + e.toString());
        }
    }
    public void startWifi(View view) {
        wifiManager.setWifiEnabled(true);
        sleep(2000);
        Message msg = new Message();
        msg.obj = "startWifi function finished";
        Message msg2 = new Message();
        msg2.obj = "startWifi function finished2";
        handler.sendMessage(msg);
        customThreadHandler.sendMessage(msg2);
    }

    public void stopWifi(View view) {
        wifiManager.setWifiEnabled(false);
        new Thread(() -> sleep(2000), "wifiOffThread").start();
        Message msg = new Message();
        msg.obj = "stopWifi function finished";
        Message msg2 = new Message();
        msg2.obj = "stopWifi function finished2";
        handler.sendMessage(msg);
        customThreadHandler.sendMessage(msg2);
    }
}
