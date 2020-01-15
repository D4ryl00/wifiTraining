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
    WifiReceiver wifiReceiver = new WifiReceiver();
    Handler handler;
    CustomThread customThread;
    Handler customThreadHandler;
    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appLogView = findViewById(R.id.appLogTextView);
        appLogView.setMovementMethod(new ScrollingMovementMethod());

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
                printLog("mainThreadHandle function");
                String log = (String) inputMessage.obj;
                printLog(log);
            }
        };

        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        customThread = new CustomThread(handler);
        customThread.start();
        customThreadHandler = new Handler(customThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                printLog("CustomThreadHandle function");
                CustomThread.sendToMainThread((String) msg.obj);
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

    public void appendLogText(String log) {
        PrintString.printString(log);
        appLogView.append(log + "\n");
    }

    private void printLog(String log) {
        appendLogText("[" + Thread.currentThread().getName() + "] " + log);
    }

    private void sendStringToHandler(String str, Handler handler) {
        Message msg = handler.obtainMessage();
        msg.obj = str;
        msg.sendToTarget();
    }

    private void mSleep(long time) {
        String prefix = "[" + Thread.currentThread().getName() + "] ";
        try {
            Thread.sleep(time);
            sendStringToHandler(prefix + "sleep 1500",
                    handler);
        }
        catch (InterruptedException e) {
            printLog(prefix + "sleep error" + e.toString());
        }
    }

    public void startWifi(View view) {
        String str = "startWifi function";
        printLog(str);
        sendStringToHandler(str, handler);
        sendStringToHandler(str, customThreadHandler);
        wifiManager.setWifiEnabled(true);
        mSleep(2000);
    }

    public void stopWifi(View view) {
        String str = "stopWifi function";
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
