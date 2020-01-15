package com.example.wifitraining;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.net.wifi.WifiManager.EXTRA_WIFI_STATE;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;

public class WifiReceiver extends BroadcastReceiver {

    private BroadcasterReceiveListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        int wifiStatus = intent.getIntExtra(EXTRA_WIFI_STATE, -1);
        switch (wifiStatus) {
            case WIFI_STATE_ENABLED:
                listener.onReceive("- WifiReceiver: wifi on");
                break ;
            case WIFI_STATE_ENABLING:
                listener.onReceive("- WifiReceiver: wifi enabling");
                break ;
            case WIFI_STATE_DISABLED:
                listener.onReceive("- WifiReceiver: wifi off");
                break ;
            case WIFI_STATE_DISABLING:
                listener.onReceive("- WifiReceiver: wifi disabling");
                break ;
            default:
                listener.onReceive("- WifiReceiver state error: unknown");
        }
    }

    public void setListener (BroadcasterReceiveListener listener){
        this.listener = listener;
    }
}
