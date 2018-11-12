package com.ucast.screen_program.socket.MessageProtocol;


import android.util.Log;

import com.ucast.screen_program.UpdateService;
import com.ucast.screen_program.jsonObject.ProgramJsonObj;
import com.ucast.screen_program.socket.Message.MessageBase;
import com.ucast.screen_program.socket.TimerConnect.WhileCheckClient;
import com.ucast.screen_program.tools.FileTools;
import com.ucast.screen_program.tools.MyScreenTools;
import com.ucast.screen_program.xutlsEvents.TishiMsgEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.netty.channel.Channel;

/**
 * Created by Administrator on 2016/2/3.
 */
public class StationPackage extends Package {

    private StringBuffer sBuffer;

    public StationPackage(Channel _channel) {
        super(_channel);
        sBuffer = new StringBuffer();
    }

    @Override
    public void Import(byte[] buffer, int Offset, int count) throws Exception {
        sBuffer.append(new String(buffer));
        int offset = 0;
        while (sBuffer.length() > offset && !mDispose) {
            int startIndex = sBuffer.indexOf("@", offset);
            if (startIndex == -1)
                break;
            int endIndex = sBuffer.indexOf("$", startIndex);
            if (endIndex == -1)
                break;
            int len = endIndex + 1;
            String value = sBuffer.substring(startIndex, len);
            //todo  协议通信
            OnMessageDataReader(value);
            offset = len;
        }
        sBuffer.delete(0, offset);
    }

    @Override
    public MessageBase MessageRead(byte[] data) {
        return null;
    }
    int lastIndex = -1;//锁定的上一个节目号
    private static final String TAG = "StationPackage";
    public MessageBase MessageRead(String value) throws Exception {
        try {
            String msg = value.substring(1, value.length() - 1);
            String[] item = msg.split(",");
            MessageBase mbase = null;
            switch (item[0]) {
                case "100": //平板播放的广告名
                    String padVideoName = item[1];
                    if ( UpdateService.screen != null && UpdateService.screen.isConnected()){
                        List<Integer> ids = new ArrayList<>();
                        List<String> programs = UpdateService.screen.readProgramList();
                        for (int i = 0; i < programs.size(); i++) {
                            String name = programs.get(i);
                            if (name.contains("P")) {
                                ids.add(MyScreenTools.getIdByProgramName(name));
                            }
                        }

                        int id = Integer.parseInt(padVideoName) % 999;
                        FileTools.writeToLogFile(id + "--");
                        if (isExitInListID(id, ids)) {
                            if (lastIndex != -1) {
                                if (isExitInListID(lastIndex,ids)) {
                                    UpdateService.screen.unlockProgram(lastIndex);
                                    try {
                                        Thread.sleep(200);
                                    }catch (Exception e){

                                    }
//                                    FileTools.writeToLogFile("解锁id为--" + lastIndex + "--");
                                }
                            }
//                            FileTools.writeToLogFile("锁定id为--" + id + "--");
                            UpdateService.screen.lockProgram(id, 32);
                            lastIndex = id;
                        }
                    }
//                    EventBus.getDefault().post(new TishiMsgEvent(padVideoName));
                    break;
                default:
                    break;

            }
            WhileCheckClient.HeartbeatTimeUpdate();
            if (mbase == null)
                return null;
            mbase.Load(item);
            return mbase;
        } catch (Exception e) {
            FileTools.writeToLogFile(e.toString());
            return null;
        }
    }


    public boolean isExitInListID(int one, List<Integer> ids){
        if (ids.size() <= 0){
            return false;
        }
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == one){
                return true;
            }
        }
        return false;
    }

}
