package com.ucast.screen_program.entity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.WindowManager;

import com.ucast.screen_program.app.ExceptionApplication;

/**
 * Created by pj on 2018/10/31.
 */
public class MyDialog {
    public static Dialog showUpdateResult(String msg){
        AlertDialog.Builder builder=new AlertDialog.Builder(ExceptionApplication.getInstance());
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);

        return alertDialog;
    }
}
