package com.ucast.screen_program.tools;

import com.ucast.screen_program.app.CrashHandler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pj on 2018/6/5.
 */
public class FileTools {

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
    public static void writeToLogFile( String data){
        try{
            File f = new File(CrashHandler.ALBUM_PATH + "/simple_log.txt");
            FileOutputStream fout = new FileOutputStream(f , true);
            BufferedOutputStream buff = new BufferedOutputStream(fout);
            buff.write((millisToDateString(System.currentTimeMillis()) + " " + data + "\r\n").getBytes());
            buff.flush();
            buff.close();
        }catch (Exception e){

        }
    }

    public static String millisToDateString(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date;
        Date curDate = new Date(time);
        date = formatter.format(curDate);
        return date;
    }
}
