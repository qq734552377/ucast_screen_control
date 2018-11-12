package com.ucast.screen_program.tools;

import android.icu.util.Output;

import com.ucast.screen_program.app.CrashHandler;
import com.ucast.screen_program.xutlsEvents.TishiMsgEvent;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by pj on 2018/6/5.
 */
public class FileTools {

    public static final String INPUTIO = "/sys/devices/platform/avrctl/input_io";
    public static final String OUTPUT1 = "/sys/devices/platform/avrctl/output_1";

    public static void writeToFile(String path , String data){
        try{
            File f = new File(path);
            FileOutputStream fout = new FileOutputStream(f , true);
            BufferedOutputStream buff = new BufferedOutputStream(fout);
            buff.write((data + "\r\n").getBytes());
            buff.flush();
            buff.close();
        }catch (Exception e){

        }
    }

    public static void clickPad(long time){
        FileTools.setOutput1Off();
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FileTools.setOutput1On();
    }

    public static void writeToLogFile( String data){
        writeToFile(CrashHandler.ALBUM_PATH + "/simple_log.txt",millisToDateString(System.currentTimeMillis()) + " " + data);
    }

    public static String millisToDateString(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date;
        Date curDate = new Date(time);
        date = formatter.format(curDate);
        return date;
    }

    public static void  cmd(String command) {
        Runtime r = Runtime.getRuntime();
        Process p;
        try {
            p = r.exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));
            StringBuilder allResults = new StringBuilder();
            String inline;
            while ((inline = br.readLine()) != null) {
                allResults.append(inline);
            }
            FileTools.writeToLogFile(allResults.toString());
            br.close();
            p.waitFor();
        } catch (IOException e) {
            FileTools.writeToLogFile(e.toString());
        }catch (InterruptedException e) {
            FileTools.writeToLogFile(e.toString());
        }
    }

    public static void sendOrderToDeviceFile(String filePath,String order){
        File f = new File(filePath);
        if (!f.exists()) {
//            EventBus.getDefault().post(new TishiMsgEvent("文件不存在"));
            return;
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            out.write(order.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
//            EventBus.getDefault().post(new TishiMsgEvent("写文件错误"));
        }
    }

    public static String loadFileAsString(String filePath) throws java.io.IOException{
        if (! new File(filePath).exists())
            return "";
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024]; int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    public static boolean getPadIsOpen(){
        String str= SavePasswd.getInstace().getIp(SavePasswd.PADSTATUS,SavePasswd.CLOSE);
        if (str.equals(SavePasswd.CLOSE))
            return false;
        else
            return true;
    }

    public static void setOutput1On(){
        String order = "on\n";
        sendOrderToDeviceFile(OUTPUT1,order);
    }

    public static void setOutput1Off(){
        String order = "off\n";
        sendOrderToDeviceFile(OUTPUT1,order);
    }
}
