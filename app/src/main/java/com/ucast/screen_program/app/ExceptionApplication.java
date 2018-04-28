package com.ucast.screen_program.app;

import android.app.Application;
import android.content.Context;
import org.xutils.x;

import j2a.awt.AwtEnv;
import onbon.bx06.Bx6GEnv;


/**
 * Created by Administrator on 2016/6/12.
 */
public class ExceptionApplication extends Application {

    public static Context context;
    private boolean initial;

    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
        try {
            // java.awt for android 初始化
            AwtEnv.link(this);                          // 將 Application 與 AWT 連結
            AwtEnv.configPaintAntiAliasFlag(true);      // 設定圖案是要抗鋸齒。
            // 建立 BX6G API 運行環境。
            Bx6GEnv.initial();
            this.initial = true;
        } catch (Exception ex) {
            this.initial = false;
        }
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        context=this;
    }
    public static Context getInstance(){
        return context;
    }

}
