package com.example.wifitraining;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;


public class CustomThread extends HandlerThread {

    private static Handler mainHandler;

    CustomThread(Handler handler) {
        super("CustomThread");
        mainHandler = handler;
    }

    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            sendToMainThread((String) msg.obj);
        }
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        sendToMainThread("set up");
    }

    public static void sendToMainThread(String string) {
        Message message = new Message();
        message.obj = "[" + Thread.currentThread().getName() + "] " + string;
        mainHandler.sendMessage(message);
    }
}
