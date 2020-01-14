package com.example.wifitraining;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

public class CustomThreadRunnable implements Runnable {

    private Handler mainHandler;
    private Handler handler;
    private Message message;

    CustomThreadRunnable(Handler handler) {
        mainHandler = handler;
    }
    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        sendToMainThread("set up");
        Looper.prepare();
        handler = new Handler() {
            public void handleMessage(Message msg) {
                sendToMainThread((String) msg.obj);
            }
        };
        Looper.loop();
    }

    private void sendToMainThread(String string) {
        message = new Message();
        message.obj = "[" + Thread.currentThread().getName() + "] " + string;
        mainHandler.sendMessage(message);
    }
}
