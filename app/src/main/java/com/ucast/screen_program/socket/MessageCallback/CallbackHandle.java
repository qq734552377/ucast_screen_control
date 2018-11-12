package com.ucast.screen_program.socket.MessageCallback;

import android.os.Environment;

import com.ucast.screen_program.socket.Message.MessageBase;

import io.netty.channel.Channel;

/**
 * Created by Administrator on 2016/2/4.
 */
public class CallbackHandle implements IMsgCallback {

    public static final String LOG_PATH = Environment.getExternalStorageDirectory().toString() + "/Ucast";

    @Override
    public void Receive(Channel channel, Object obj) {
        if (obj == null)
            return;
        if (!(obj instanceof MessageBase))
            return;
        MessageBase msgbase = (MessageBase) obj;
        switch (msgbase.Cmd) {
            case "1105":
                break;
            default:
                break;
        }
    }

    private static final String TAG = "CallbackHandle";






}
