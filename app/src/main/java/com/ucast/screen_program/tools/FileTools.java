package com.ucast.screen_program.tools;

import com.ucast.screen_program.app.CrashHandler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
}
