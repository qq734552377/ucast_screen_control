package com.ucast.screen_program.entity;

import com.ucast.screen_program.UpdateService;
import com.ucast.screen_program.mytime.MyTimeTask;
import com.ucast.screen_program.mytime.MyTimer;
import com.ucast.screen_program.tools.MyHttpRequetTool;

import de.greenrobot.event.EventBus;
import onbon.bx06.Bx6GScreenClient;

/**
 * Created by pj on 2018/5/2.
 */
public class ReConnectScreen {
    private static MyTimer timer;

    private static boolean restart;

    private static long oldTime;

    private final static long notifyUpdateScreenPeriod = 90 * 1000L;

    public static void startTimer() {
        timer = new MyTimer(new MyTimeTask(new Runnable() {
            public void run() {
                synchronized (ReConnectScreen.class) {
                    try {
                        if (!restart)
                            return;
                        Bx6GScreenClient screenClient = UpdateService.screen;
                        if (screenClient == null){
                            UpdateService.screen = new Bx6GScreenClient("UcastScreen");
                        }
                        if (screenClient.isConnected()){
                            if ((System.currentTimeMillis() - oldTime) > notifyUpdateScreenPeriod) {
                                oldTime = System.currentTimeMillis();
                                MyHttpRequetTool.getAllPrograms(ScreenHttpRequestUrl.DOWNLOADFILEURL);
                            }
                            return;
                        }
                        connect(screenClient);
                        oldTime = System.currentTimeMillis();
                        restart = false;
                    } catch (Exception e) {
                        restart = false;
                    }
                }
            }
        }), 2000L, 15000L);
        timer.initMyTimer().startMyTimer();
    }

    public static void check() {
        synchronized (ReConnectScreen.class) {
            restart = true;
        }
    }

    public static void stopTimer(){
        synchronized (ReConnectScreen.class) {
            restart = true;
            timer.stopMyTimer();
        }
    }

    public static void connect(Bx6GScreenClient screen){
        screen.connect(Config.ScreenServer,Config.ScreenServerPort);
    }
}
