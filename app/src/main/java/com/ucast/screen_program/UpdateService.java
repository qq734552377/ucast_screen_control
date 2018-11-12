package com.ucast.screen_program;

import android.app.Dialog;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ucast.screen_program.app.ExceptionApplication;
import com.ucast.screen_program.entity.Config;
import com.ucast.screen_program.entity.MyDialog;
import com.ucast.screen_program.entity.ReConnectScreen;
import com.ucast.screen_program.entity.ScreenHttpRequestUrl;
import com.ucast.screen_program.jsonObject.BaseHttpResult;
import com.ucast.screen_program.mytime.MyTimeTask;
import com.ucast.screen_program.mytime.MyTimer;
import com.ucast.screen_program.socket.TimerConnect.WhileCheckClient;
import com.ucast.screen_program.tools.FileTools;
import com.ucast.screen_program.tools.MyHttpRequetTool;
import com.ucast.screen_program.entity.MyScreenUpdateTask;
import com.ucast.screen_program.tools.SavePasswd;
import com.ucast.screen_program.xutlsEvents.RunTaskEvent;
import com.ucast.screen_program.xutlsEvents.TishiMsgEvent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import onbon.bx06.Bx6GScreenClient;


/**
 * Created by pj on 2016/11/21.
 */
public class UpdateService extends Service {

    public static ThreadPoolExecutor poolExecutor;
    public static Bx6GScreenClient screen;
    public static WifiManager wifiManager;
    private Dialog msgDialog;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return this.START_STICKY;
    }

    @Override
    public void onCreate() {

        Notification notification = new Notification();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        startForeground(1, notification);
        super.onCreate();
//        startTimer();
        copyCfg("pic2.jpg");
        copyCfg("pic4.jpg");
        File file = new File(Config.PICPATHDIR);
        if (!file.exists()){
            file.mkdir();
        }
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);


        poolExecutor = new ThreadPoolExecutor(1, 1,
                0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(128));
        EventBus.getDefault().register(this);

        //开启节目请求
        ReConnectScreen.startTimer();
//        MyHttpRequetTool.getAllPrograms(ScreenHttpRequestUrl.DOWNLOADFILEURL);
        WhileCheckClient.StartTimer();

        startTimer();
    }

    public static void writeToScreen(Runnable task){
        if (poolExecutor == null || task == null)
            return;
        poolExecutor.execute(task);
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void showTishiMsg(TishiMsgEvent event){
        msgDialog = MyDialog.showUpdateResult(event.getMsg());
        msgDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void addTaskToQueue(RunTaskEvent msg){

        if (msg.getTask() == null)
            writeToScreen(new MyScreenUpdateTask(screen));
        else
            writeToScreen(msg.getTask());
    }


    /**
     * 当服务被杀死时重启服务
     * */
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        stopForeground(true);
        Intent localIntent = new Intent();
        localIntent.setClass(this, UpdateService.class);
        this.startService(localIntent);    //销毁时重新启动Service
    }

    public MyTimer timer;
    public void startTimer() {
        timer = new MyTimer(new MyTimeTask(new Runnable() {
            @Override
            public void run() {
                String io = null;
                try {
                    io = FileTools.loadFileAsString(FileTools.INPUTIO);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int ioInt = Integer.parseInt(io.replace("\n",""));
                boolean isFire = (ioInt & 0x01) == 0 ? true : false;

                boolean isPadOpen = FileTools.getPadIsOpen();

//                EventBus.getDefault().post(new TishiMsgEvent(ioInt + (isFire ? "点火" : "熄火") + "  平板状态" + (isPadOpen ? "平板亮屏" : "平板息屏")));
                if (isFire && !isPadOpen){//点火状态并且平板是息屏状态  点亮平板
//                    FileTools.clickPad(3000);
                    setPadState(true);
                }else if (!isFire && isPadOpen){//熄火状态并且平板是亮屏状态  平板息屏
                    FileTools.clickPad(200);
                    setPadState(false);
                }


            }
        }), 1000*2L, 10*1000L);
        timer.initMyTimer().startMyTimer();
    }

    public void setPadState(boolean isPadOpen){
        if (isPadOpen){//平板不是亮屏，点亮
            SavePasswd.getInstace().save(SavePasswd.PADSTATUS,SavePasswd.OPEN);
        }else {//平板是亮屏，息屏
            SavePasswd.getInstace().save(SavePasswd.PADSTATUS,SavePasswd.CLOSE);
        }
    }

    private static final String TAG = "UpdateService";
    public void getSystemTime(String url){
        if (!MyHttpRequetTool.isNetworkAvailable(ExceptionApplication.getInstance()))
            return;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("Sn", Config.STATION_ID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseHttpResult base = JSON.parseObject(result, BaseHttpResult.class);
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



    public void setTime(String mytime){
        Date mydate=StringToDate(mytime);
        long curMs=mydate.getTime();
        boolean isSuc = SystemClock.setCurrentTimeMillis(curMs);//需要Root权限
        Log.e(TAG, "setTime: "+isSuc );
    }
    private Date StringToDate(String s){
        Date time=null;
        SimpleDateFormat sd=new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            time=sd.parse(s);
        } catch (java.text.ParseException e) {
            System.out.println("输入的日期格式有误！");
            e.printStackTrace();
        }
        return time;
    }

    public  void copyCfg(String picName) {
        String dirPath = Environment.getExternalStorageDirectory().getPath() + "/"+picName;
        FileOutputStream os = null;
        InputStream is = null;
        int len = -1;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream("assets/"+picName);
            os = new FileOutputStream(dirPath);
            byte b[] = new byte[1024];
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
            is.close();
            os.close();
        } catch (Exception e) {
        }
    }

}
