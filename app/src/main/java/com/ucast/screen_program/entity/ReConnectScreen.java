package com.ucast.screen_program.entity;

import com.ucast.screen_program.UpdateService;
import com.ucast.screen_program.app.ExceptionApplication;
import com.ucast.screen_program.mytime.MyTimeTask;
import com.ucast.screen_program.mytime.MyTimer;
import com.ucast.screen_program.tools.FileTools;
import com.ucast.screen_program.tools.MyHttpRequetTool;

import java.io.File;

import de.greenrobot.event.EventBus;
import onbon.bx06.Bx6GScreenClient;

/**
 * Created by pj on 2018/5/2.
 */
public class ReConnectScreen {
    private static MyTimer timer;


    private static long oldTime;

    private final static long notifyUpdateScreenPeriod = 10 * 1000L;

    public static void startTimer() {
        timer = new MyTimer(new MyTimeTask(new Runnable() {
            public void run() {
                synchronized (ReConnectScreen.class) {
                    try {
                        Bx6GScreenClient screenClient = UpdateService.screen;
                        if (screenClient == null){
                            UpdateService.screen = new Bx6GScreenClient("UcastScreen");
                            return;
                        }
                        if (screenClient.isConnected()){
//                            FileTools.writeToLogFile("已连接上灯板-->");
                            if ((System.currentTimeMillis() - oldTime) > notifyUpdateScreenPeriod) {
                                if(!MyHttpRequetTool.isWiFi(ExceptionApplication.getInstance())){
                                    UpdateService.wifiManager.setWifiEnabled(true);
                                }
                                oldTime = System.currentTimeMillis();
//                                FileTools.writeToFile(Config.LOGFILEPATH,FileTools.millisToDateString(System.currentTimeMillis()) + "请求一次服务器");
                                MyHttpRequetTool.getAllPrograms(ScreenHttpRequestUrl.DOWNLOADFILEURL);
                            }
                            return;
                        }
//                        FileTools.writeToLogFile("没有连接上灯板上灯板--> 尝试连接灯板");
                        connect(screenClient);
                        oldTime = System.currentTimeMillis();
                    } catch (Exception e) {
                        FileTools.writeToLogFile("定时器异常");
                    }
                }
            }
        }), 2000L, 5000L);
        timer.initMyTimer().startMyTimer();
    }

    public static void check() {
        synchronized (ReConnectScreen.class) {
        }
    }

    public static void stopTimer(){
        synchronized (ReConnectScreen.class) {
            timer.stopMyTimer();
        }
    }

    public static void connect(Bx6GScreenClient screen) {
        try {
            screen.disconnect();
            UpdateService.wifiManager.setWifiEnabled(false);
            Thread.sleep(2000);
            boolean isCon = screen.connect(Config.ScreenServer, Config.ScreenServerPort);
            if(isCon) {
                FileTools.writeToFile(Config.LOGFILEPATH, FileTools.millisToDateString(System.currentTimeMillis()) + "连接之后尝试开启wifi");
                UpdateService.wifiManager.setWifiEnabled(true);
            }else{
                FileTools.writeToFile(Config.LOGFILEPATH, FileTools.millisToDateString(System.currentTimeMillis()) + "没有连接上灯板");
            }

        } catch (Exception e) {
        }
    }

}
