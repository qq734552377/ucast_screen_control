package com.ucast.screen_program.entity;


import com.ucast.screen_program.app.CrashHandler;

/**
 * Created by Administrator on 2016/1/20.
 */
public class Config {
    public static String STATION_ID = "SN98787";
    public final static String ScreenServer = "192.168.3.1";
    public final static int ScreenServerPort = 5005;
    public final static String PrinterSerialName = "ttymxc4";
    public final static String UsbWithByteSerialName = "g_print0";
    public final static String NETPrintName = "ucast_net_print";
    public final static String KeyboardSerialName = "hidg0";
    public final static String PrinterSerial = "/dev/ttymxc4";
    public final static String KeyboardSerial = "/dev/hidg0";
    public final static String UsbSerial = "/dev/g_printer0";
    public final static int PRINT_BAIDRATE = 115200 * 4;
    public final static int USB_BAIDRATE = 115200;
    public final static int NET_PRINT_PORT = 43078;
    public static final String PICPATHDIR =  CrashHandler.ALBUM_PATH + "/pic";
    public static final String LOGFILEDIR =  CrashHandler.ALBUM_PATH + "/pic";
    public static final String LOGFILEPATH = LOGFILEDIR + "/msgs.log";



}
