package com.ucast.screen_program;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ucast.screen_program.entity.ReConnectScreen;
import com.ucast.screen_program.socket.TimerConnect.WhileCheckClient;
import com.ucast.screen_program.tools.FileTools;
import com.ucast.screen_program.tools.WifiConnect;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.on).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                WhileCheckClient.Run("Airport", "1234567890", "192.168.43.1");
//                ReConnectScreen.wifiConnect.connect(WifiConnect.WIFI_NAME,WifiConnect.WIFI_PWD,WifiConnect.WifiCipherType.WIFICIPHER_NOPASS);
            }
        });
        findViewById(R.id.off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileTools.clickPad(200);
            }
        });
        Intent ootStartIntent = new Intent(this, UpdateService.class);
        ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(ootStartIntent);
    }
}
