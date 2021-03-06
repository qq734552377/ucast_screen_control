package com.ucast.screen_program.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alibaba.fastjson.JSON;
import com.ucast.screen_program.app.ExceptionApplication;
import com.ucast.screen_program.entity.Config;
import com.ucast.screen_program.entity.ReConnectScreen;
import com.ucast.screen_program.jsonObject.BaseHttpResult;
import com.ucast.screen_program.jsonObject.ProgramJsonObj;
import com.ucast.screen_program.xutlsEvents.RunTaskEvent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by pj on 2018/4/28.
 */
public class MyHttpRequetTool {
    public static List<ProgramJsonObj> msgs = null;


    public static void getAllPrograms(String url){
        if (!WifiConnect.isWifiConnect()){
//            ReConnectScreen.wifiConnect.connect(WifiConnect.WIFI_NAME,WifiConnect.WIFI_PWD,WifiConnect.WifiCipherType.WIFICIPHER_NOPASS);
        }
        if (!isNetworkAvailable(ExceptionApplication.getInstance())) {
            return;
        }
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("Sn", Config.STATION_ID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseHttpResult base = JSON.parseObject(result, BaseHttpResult.class);
                if (base.getMsgType().equals("Success")) {
                    msgs = JSON.parseArray(base.getData(), ProgramJsonObj.class);
                    EventBus.getDefault().postSticky(new RunTaskEvent(null,"this is getAllPrograms success"));
//                    FileTools.writeToLogFile("this is getAllPrograms success" + msgs.size());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static void downLoadOnePic (String url,final String filePath){
        if (!isNetworkAvailable(ExceptionApplication.getInstance()))
            return;
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(filePath);
        x.http().get(requestParams, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                EventBus.getDefault().postSticky(new RunTaskEvent(null,"this is downLoadOnePic success"));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }



    //判断手机上所有的网络设备是否可用
    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //判断WiFi是否打开
    public static boolean isWiFi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    //判断移动数据是否打开
    public static boolean isMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }
}
