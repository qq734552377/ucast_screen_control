package com.ucast.screen_program.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.ucast.screen_program.app.ExceptionApplication;

import java.io.IOException;
import java.util.List;

public class WifiConnect {

	WifiManager wifiManager;
	public static final String WIFI_NAME= "Airport1";
	public static final String WIFI_PWD= "1234567890";

	public enum WifiCipherType {
		WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID,WPA2_PSK
	}


	public WifiConnect(WifiManager wifiManager) {
		this.wifiManager = wifiManager;
	}

	public boolean openWifi() {
		boolean bRet = true;
		if (!wifiManager.isWifiEnabled()) {
			bRet = wifiManager.setWifiEnabled(true);
		}
		return bRet;
	}

	public boolean closeWifi(){
		boolean bRet = true;
		if (wifiManager.isWifiEnabled()){
			bRet = wifiManager.setWifiEnabled(!bRet);
		}
		return bRet;
	}


	public boolean connect(String SSID, String Password, WifiCipherType Type) {
		boolean bRet = false;
		try {
			if (!this.openWifi()) {
				return false;
			}
			while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
				try {
//				Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException ie) {

				}
			}

			WifiConfiguration wifiConfig = this.CreateWifiInfo(SSID, Password, Type);

			if (wifiConfig == null) {
				Log.e("calm", "-------配置Null------");
				return false;

			}

			WifiConfiguration tempConfig = this.IsExsits(SSID);

			if (tempConfig != null) {
				wifiManager.removeNetwork(tempConfig.networkId);
			}

			int netID = wifiManager.addNetwork(wifiConfig);
			bRet = wifiManager.enableNetwork(netID, true);

		}catch (Exception e){

		}finally {
			return bRet;
		}

	}
	// WiFi 是否连接
	public static boolean isWifiConnect() {
		Context context=ExceptionApplication.context;
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}

	private WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
		if(existingConfigs==null)
			return null;
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	private WifiConfiguration CreateWifiInfo(String SSID, String Password, WifiCipherType Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
			//config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			//config.wepTxKeyIndex = 0;
		}
		if (Type == WifiCipherType.WIFICIPHER_WEP) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == WifiCipherType.WIFICIPHER_WPA) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
			
		}else if(Type == WifiCipherType.WPA2_PSK){
            config.preSharedKey = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.status = WifiConfiguration.Status.ENABLED;
        } else {
			return null;
		}
		return config;
	}

	public boolean pingNet(){
		boolean result = false;
		try {
			Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + "www.baidu.com");
			int status = p.waitFor();
			if (status == 0) {
				result = true;
			}
		} catch (IOException e) {

		} catch (InterruptedException e) {

		}finally {
			return result;
		}

	}

}