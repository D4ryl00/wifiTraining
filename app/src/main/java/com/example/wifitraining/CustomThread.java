package com.example.wifitraining;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import printString.PrintString;

/*
* CustomThread extends HandlerThread because it's a helper class that comes with a looper.
 */
public class CustomThread extends HandlerThread {

    private static Handler mainHandler;

    CustomThread(Handler handler) {
        super("customThread");
        mainHandler = handler;
    }

    public static void sendToMainThread(String string) {
        PrintString.printString(string);
        Message message = new Message();
        message.obj = string + " [" + Thread.currentThread().getName() + ": sendToMainThread]" ;
        mainHandler.sendMessage(message);
    }
}
