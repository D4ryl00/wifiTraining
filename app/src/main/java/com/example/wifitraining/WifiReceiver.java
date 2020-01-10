package com.example.wifitraining;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import printString.PrintString;

import static android.net.wifi.WifiManager.EXTRA_WIFI_STATE;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;

public class WifiReceiver extends BroadcastReceiver {

    private BroadcasterReceiveListencer listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        int wifiStatus = intent.getIntExtra(EXTRA_WIFI_STATE, -1);
        switch (wifiStatus) {
            case WIFI_STATE_ENABLED:
                listener.onReceive("WifiReceiver: wifi on");
                break ;
            case WIFI_STATE_DISABLED:
                listener.onReceive("WifiReceiver: wifi off");
                break ;
            default:
                PrintString.printString("WifiReceiver error: failed to get extra");
        }
    }

    public void setListener (BroadcasterReceiveListencer listener){
        this.listener = listener;
    }
}
