package com.ucast.screen_program.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alibaba.fastjson.JSON;
import com.ucast.screen_program.app.ExceptionApplication;
import com.ucast.screen_program.entity.Config;
import com.ucast.screen_program.jsonObject.BaseHttpResult;
import com.ucast.screen_program.jsonObject.ProgramJsonObj;

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
        if (!isNetworkAvailable(ExceptionApplication.getInstance()))
            return;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("Sn", Config.STATION_ID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseHttpResult base = JSON.parseObject(result, BaseHttpResult.class);
                msgs = JSON.parseArray(base.getData(), ProgramJsonObj.class);
                for (int i = 0; i <msgs.size() ; i++) {
                    FileTools.writeToFile(Config.LOGFILEPATH,msgs.get(i).toString() + "-------------------");
                }
                EventBus.getDefault().postSticky("this is getAllPrograms success");
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
                EventBus.getDefault().postSticky("this is downLoadOnePic success");
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
}
