package com.example.wifitraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import printString.PrintString;

public class MainActivity extends AppCompatActivity implements BroadcasterReceiveListener {

    TextView appLogView;
    WifiReceiver wifiReceiver;
    Handler handler;
    CustomThread customThread;
    Handler customThreadHandler;
    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable scrolling for the log view
        appLogView = findViewById(R.id.appLogTextView);
        appLogView.setMovementMethod(new ScrollingMovementMethod());

        printLog(PrintString.printString("Hello world"));

        Thread.currentThread().setName("mainThread");

        // Create IntentFilter for the wifi BroadcasterReceiver
        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        wifiReceiver = new WifiReceiver();
        this.registerReceiver(wifiReceiver, filter);
        wifiReceiver.setListener(this);

        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Set new main function Handler
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                String log = (String) inputMessage.obj;
                printLog(log + " [mainHandler]");
            }
        };

        // Start new thread with looper and handler
        customThread = new CustomThread(handler);
        customThread.start();
        customThreadHandler = new Handler(customThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                CustomThread.sendToMainThread(msg.obj + " [customHandler]");
            }
        };
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

    private void appendLogText(String log) {
        PrintString.printString(log);
        appLogView.append(log + "\n");
    }

    private void printLog(String log) {
        appendLogText(log + " [" + Thread.currentThread().getName() + "]");
    }

    private void sendStringToHandler(String str, Handler handler) {
        Message msg = handler.obtainMessage();
        msg.obj = str;
        msg.sendToTarget();
    }

    private void mSleep(long time) {
        String suffix = " [" + Thread.currentThread().getName() + "]";
        try {
            Thread.sleep(time);
            sendStringToHandler("- sleep 1500" + suffix,
                    handler);
        }
        catch (InterruptedException e) {
            printLog("- sleep error" + e.toString() + suffix);
        }
    }

    public void startWifi(View view) {
        String str = "- startWifi function";
        printLog(str);
        sendStringToHandler(str, handler);
        sendStringToHandler(str, customThreadHandler);
        wifiManager.setWifiEnabled(true);
        mSleep(2000);
    }

    public void stopWifi(View view) {
        String str = "- stopWifi function";
        printLog(str);
        sendStringToHandler(str, handler);
        sendStringToHandler(str, customThreadHandler);
        Thread wifiThread = new Thread(
                () -> {wifiManager.setWifiEnabled(false); mSleep(2000); },
                "wifiOffThread"
        );
        wifiThread.start();
    }
}
