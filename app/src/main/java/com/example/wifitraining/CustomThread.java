package com.example.wifitraining;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import printString.PrintString;


public class CustomThread extends HandlerThread {

    private static Handler mainHandler;

    CustomThread(Handler handler) {
        super("CustomThread");
        mainHandler = handler;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        sendToMainThread("set up");
    }

    public static void sendToMainThread(String string) {
        PrintString.printString(string);
        Message message = new Message();
        message.obj = "[" + Thread.currentThread().getName() + "] " + string;
        mainHandler.sendMessage(message);
    }
}
