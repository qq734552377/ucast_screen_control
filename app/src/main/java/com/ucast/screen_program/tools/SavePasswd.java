package com.ucast.screen_program.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Xml;

import com.ucast.screen_program.app.CrashHandler;
import com.ucast.screen_program.app.ExceptionApplication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 * Created by Administrator on 2016/6/12.
 */
public class SavePasswd {

    private static SavePasswd savePasswd;
    private final String fileName = "myfile";
    private final String xmlPath = CrashHandler.ALBUM_PATH +
                    "/UcastSetting.xml";

    public final static String XMLSTARTTAG = "setting";
    public final static String ISOPENPRINT = "isopenprint";//是否开启打印功能
    public final static String ISAUTOMONEYBOX = "isautomoneybox";//是否自动打开钱箱
    public final static String ISMAINSERVICE = "ismainservice";//是否上传到主服务器
    public final static String BMPUPLOADHOST = "bmpuploadhost";//图片的上传主机地址
    public final static String HEARTBEATHOST = "heartbeathost";//心跳上传的主机地址
    public final static String ISNETPRINTUPLOADTOSERVICE = "isnetprintuploadtoservice";//网络打印的图片是否上传服务器

    public final static String ADVIMGURL = "adv_img_url";//广告的url
    public final static String ADVVIDEOURL = "adv_video_url";//广告vedio的url
    public final static String ADVIMGTITLE = "adv_img_title";//广告的title
    public final static String ALLADVDATA = "alladvdata";//获取的Data数据
    public final static String MONEYTIME = "money_box_time";//广告的title
    public final static String LASTREQUESTTIME = "last_request_time";//最后设备请求服务器的时间
    public final static String LINKWITHPOS = "link_with_pos";//打印机是否与收银设备连接
    public final static String IS58PAPPER = "is58papper";//是否是58的纸张
    public final static String ISOPENLOG = "isopenlog";//是否开启日志记录
    public final static String PADSTATUS = "padstatus";//是否开启日志记录
    public final static String OPENPRINT = "open";
    public final static String CLOSEPRINT = "close";
    public final static String OPEN = "open";
    public final static String CLOSE = "close";


    private static String[] xmlKeys = {ISOPENPRINT,ISAUTOMONEYBOX,ISNETPRINTUPLOADTOSERVICE,BMPUPLOADHOST,HEARTBEATHOST};


    private SavePasswd() {

    }

    public static SavePasswd getInstace() {
        if (savePasswd == null) {
            synchronized (SavePasswd.class) {
                if (savePasswd == null) {
                    savePasswd = new SavePasswd();
                }
            }
        }
        return savePasswd;
    }

    public void save(String name, String passwd) {
        SharedPreferences sp = ExceptionApplication
                .context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name, passwd);
        editor.commit();
    }

    public String get(String name) {
        SharedPreferences sp = ExceptionApplication
                .context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        return sp.getString(name, "");
    }

    public String getIp(String name, String defaultValue){
        SharedPreferences sp=ExceptionApplication
                .context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        return sp.getString(name,defaultValue);
    }

    public void savexml(String key, String value) {

        try {
            File file = new File(xmlPath);
            FileOutputStream fos = new FileOutputStream(file);
            // 获得一个序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            // 设置文件头
            serializer.startDocument("utf-8", true);
            // 写姓名
            serializer.startTag(null, XMLSTARTTAG);

            int len= xmlKeys.length;
            for (int i = 0; i <len ; i++) {
                serializer.startTag(null, xmlKeys[i]);
                if (xmlKeys[i].equals(key)){
                    serializer.text(value);
                }else{
                    serializer.text(this.get(xmlKeys[i]));
                }
                serializer.endTag(null, xmlKeys[i]);
            }

            serializer.endTag(null, XMLSTARTTAG);

            serializer.endDocument();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static final String TAG = "SavePasswd";

    public String readxml(String key, String defaultValue) {
        String value = null;
        try {
            File path = new File(xmlPath);
            if (!path.exists()) {
                return getIp(key,defaultValue);
            }
            FileInputStream fis = new FileInputStream(path);

            // 获得pull解析器对象
            XmlPullParser parser = Xml.newPullParser();
            // 指定解析的文件和编码格式
            parser.setInput(fis, "utf-8");

            int eventType = parser.getEventType(); // 获得事件类型
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName(); // 获得当前节点的名称
                switch (eventType) {
                    case XmlPullParser.START_TAG: // 当前等于开始节点 <person>
                        if (key.equals(tagName)) {
                            value = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG: // </persons>
                        if (key.equals(tagName)) {
                            Log.e(TAG, "readxml: " + value);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next(); // 获得下一个事件类型
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return (value == null || value.equals("") )? defaultValue : value;
        }

    }

}
