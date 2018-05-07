package com.ucast.screen_program.entity;

import com.ucast.screen_program.jsonObject.ProgramJsonObj;
import com.ucast.screen_program.tools.MyHttpRequetTool;
import com.ucast.screen_program.tools.MyScreenTools;

import java.util.ArrayList;
import java.util.List;

import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.file.ProgramBxFile;

/**
 * Created by pj on 2018/5/2.
 */
public class MyScreenUpdateTask implements Runnable{
    Bx6GScreenClient screen = null;

    public MyScreenUpdateTask(Bx6GScreenClient screen) {
        this.screen = screen;
    }

    @Override
    public void run() {
        writeToScreen();
    }

    public void writeToScreen(){
        List<String> programs = null;
        Bx6GScreenClient curScreen = screen;
        List<ProgramJsonObj> msgs = MyHttpRequetTool.msgs;
        if(msgs == null){
            return;
        }
        if (curScreen == null || !curScreen .isConnected()){
            return;
        }
        try {
            programs = curScreen.readProgramList();

            if (programs != null) {
                List<Integer> ids = new ArrayList<>();
                for (int i = 0; i < programs.size(); i++) {
                    String name = programs.get(i);
                    if (name.contains("P")) {
                        ids.add(MyScreenTools.getIdByProgramName(name));
                    }
                }
                ids = removeUnavialablePrograms(curScreen,ids,msgs);
                for (int i = 0; i < msgs.size(); i++) {
                    ProgramJsonObj one = msgs.get(i);
                    if(!isExitInListID(one,ids)){
                        ProgramBxFile programBxFile = MyScreenTools.getOneProgramBxFileWithProgramJsonObj(curScreen.getProfile(),one);
                        if(programBxFile == null){
                            continue;
                        }
                        try {
                            curScreen.writeProgramQuickly(programBxFile);
                        } catch (Bx6GException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Integer> removeUnavialablePrograms(Bx6GScreenClient screen,List<Integer> ids ,List<ProgramJsonObj> programJsonObjs){
        List<Integer> availableIds = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            if (!isExitInProgramlists(ids.get(i),programJsonObjs)){
                screen.deleteProgram(ids.get(i));
            }else{
                availableIds.add(ids.get(i));
            }
        }
        return availableIds;
    }

    public boolean isExitInProgramlists(int id ,List<ProgramJsonObj> programJsonObjs){
        if (programJsonObjs.size() <= 0){
            return false;
        }
        for (int i = 0; i < programJsonObjs.size(); i++) {
            if (id == programJsonObjs.get(i).getId()){
                return true;
            }
        }
        return false;
    }

    public boolean isExitInListID(ProgramJsonObj one,List<Integer> ids){
        if (ids.size() <= 0){
            return false;
        }
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == one.getId()){
                return true;
            }
        }
        return false;
    }
}
