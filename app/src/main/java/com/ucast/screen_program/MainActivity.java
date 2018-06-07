package com.ucast.screen_program;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ucast.screen_program.entity.ReConnectScreen;
import com.ucast.screen_program.entity.ScreenHttpRequestUrl;
import com.ucast.screen_program.jsonObject.ProgramJsonObj;
import com.ucast.screen_program.tools.MyHttpRequetTool;
import com.ucast.screen_program.tools.MyScreenTools;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import onbon.bx06.Bx6GCommException;
import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.file.ProgramBxFile;

public class MainActivity extends AppCompatActivity {

    public static Bx6GScreenClient screen;

    private EditText outputText;

    private Button connButton;

    private Button disconnButton;

    private ToggleButton screenButton;

    private Button sendButton;

    boolean isLock = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent ootStartIntent = new Intent(this, UpdateService.class);
        ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(ootStartIntent);
        this.outputText = (EditText)findViewById(R.id.outputText);
        this.connButton = (Button)findViewById(R.id.connButton);
        this.connButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                UpdateService.poolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        connect();
                    }
                });
            }
        });

        this.disconnButton = (Button)findViewById(R.id.disconnButton);
        this.disconnButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                UpdateService.poolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        disconnect();
                    }
                });
            }
        });

        this.screenButton = (ToggleButton)findViewById(R.id.screenButton);
        this.screenButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
//                        turnOnOff();
//                        if (isLock){
////                            isLock = false;
////                            screen.unlockProgram(4);
////                        }else{
////                            isLock = true;
////                            screen.lockProgram(4,50);
////                        }
                        try {
                            List<String> programs = screen.readProgramList();
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < programs.size(); i++) {
                                String name = programs.get(i);
                                if (name.contains("P")){
                                    sb.append( MyScreenTools.getIdByProgramName(name)+ "---");
                                }
                            }
                            final String msg = sb.toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MainActivity.this.outputText.setText(msg);
                                }
                            });

                        } catch (Bx6GCommException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();
            }
        });

        this.sendButton = (Button)findViewById(R.id.sendButton);
        this.sendButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {

                UpdateService.poolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
//                        screen.	deletePrograms();

                        writeProgram();
                    }
                });
            }
        });

        this.screen = new Bx6GScreenClient("UcastScreen");

        UpdateService.screen = this.screen;

//        MyHttpRequetTool.getAllPrograms(ScreenHttpRequestUrl.DOWNLOADFILEURL);
    }

    private boolean testDeployOKorNot() {
        try {
            int size = onbon.db.Factory.getIstance().fineSeriesList().size();
            this.connButton.setTextColor(Color.BLUE);
            this.connButton.setText("OK. Series Size:" + size);
            return true;
        }
        catch (Exception ex) {
            this.connButton.setText("Failed");
            this.outputText.setText(ex.getMessage());
            return false;
        }
    }

    private void connect() {
        if(this.screen.isConnected())
            return;
        final TextView addrText = (TextView)findViewById(R.id.addrText);
        final TextView portText = (TextView)findViewById(R.id.portText);
        if (screen.connect("" + addrText.getText(), Integer.parseInt("" + portText.getText()))) {
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.connButton.setEnabled(false);
                    MainActivity.this.disconnButton.setEnabled(true);
                    MainActivity.this.sendButton.setEnabled(true);
                    MainActivity.this.screenButton.setEnabled(true);
                    MainActivity.this.outputText.setText("CONN success");
                }
            });
        }
        else {
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.outputText.setText("CONN failure");
                }
            });
        }
    }

    private void disconnect() {
//        this.screen.disconnect();
//        runOnUiThread(new Runnable() {
//            public void run() {
//                MainActivity.this.connButton.setEnabled(true);
//                MainActivity.this.disconnButton.setEnabled(false);
//                MainActivity.this.sendButton.setEnabled(false);
//                MainActivity.this.screenButton.setEnabled(false);
//            }
//        });
        UpdateService.poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                screen.	deletePrograms();
            }
        });
    }

    private void turnOnOff(){
        if(this.screenButton.isChecked()) {
            MainActivity.this.screen.turnOn();
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.outputText.setText("Turn ON");
                }
            });
        }
        else {
            this.screen.turnOff();
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.outputText.setText("Turn OFF");
                }
            });
        }
    }

    private void writeProgram() {
        EditText msgText = (EditText)findViewById(R.id.msgText);
        EditText fontNameText = (EditText)findViewById(R.id.fontNameText);
        EditText fontSizeText = (EditText)findViewById(R.id.fontSizeText);

        String filePath1 =  Environment.getExternalStorageDirectory().getPath() + "/pic2.jpg";
        String filePath2 =  Environment.getExternalStorageDirectory().getPath() + "/pic4.jpg";
        try {
            ProgramBxFile p1 = MyScreenTools.getOneProgramBxFileWithText(screen.getProfile(),231,msgText.getText().toString());
            ProgramBxFile p2 = MyScreenTools.getOneProgramBxFileWithPicpath(screen.getProfile(),783,filePath2);
            ProgramBxFile p3 = MyScreenTools.getOneProgramBxFileWithGoodPicpath(screen.getProfile(),40,filePath1);
            ProgramBxFile p4 = MyScreenTools.getOneProgramBxFileWithText(screen.getProfile(),111,"大王叫我来巡山");
            screen.writeProgramQuickly(p1);
            screen.writeProgramQuickly(p2);
            screen.writeProgramQuickly(p3);
            screen.writeProgramQuickly(p4);
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.outputText.setText("WRITE OK");
                }
            });
        }
        catch (final Exception ex) {
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.outputText.setText("WRITE failure");
                }
            });
        }
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
