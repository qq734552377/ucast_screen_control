package com.ucast.screen_program;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

import com.ucast.screen_program.socket.TimerConnect.WhileCheckClient;
import com.ucast.screen_program.xutlsEvents.TishiMsgEvent;

import de.greenrobot.event.EventBus;

import static android.content.Context.PRINT_SERVICE;
import static com.ucast.screen_program.UpdateService.wifiManager;

public class WiFiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){//wifi连接上与否 
            WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
            NetworkInfo netInfo = null;
            try {
                netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            }catch (Exception e){
                e.printStackTrace();
            }

            if (netInfo.getState().equals(NetworkInfo.State.CONNECTED)){//wifi已连接上
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                DhcpInfo info=wifiManager.getDhcpInfo();
                System.out.println();
                final String ip = intToIpv4(info.serverAddress);
                final String ssid = wifiInfo.getSSID();
                final String password = "1234567890";
//                EventBus.getDefault().post(new TishiMsgEvent("ip:" + ip + "\nssid:" + ssid ));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WhileCheckClient.Run(ssid, password, ip);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
            if (netInfo.getState().equals(NetworkInfo.State.DISCONNECTED)){
//                EventBus.getDefault().post(new TishiMsgEvent("wifi已断开"));
            }


        }
    }

    public String intToIpv4(int ip) {

        return    ((ip >> 0 ) & 0xFF)  + "."
                + ((ip >> 8 ) & 0xFF)  + "."
                + ((ip >> 16) & 0xFF)  + "."
                + ((ip >> 24) & 0xFF);
    }
}
