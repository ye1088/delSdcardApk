package com.google.delsdcardapk;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by admin on 2018/1/6.
 */

public class ButtonUtils {

    private static final String SDCARD_PATH = "/sdcard";

    public static void  delSdCardApk(Activity activity, TextView logText){
        File sdcard = new File(SDCARD_PATH);
        if (!sdcard.exists()){
            Toast.makeText(activity,"没有Sdcard或者出现莫名其妙的问题了",Toast.LENGTH_LONG).show();
            return  ;
        }

        for (File file :
                sdcard.listFiles()) {
            if (file.getName().endsWith(".apk")){
                Utils.showLog(file.getAbsolutePath());
                logText.setText(logText.getText().toString()+"\n已经删除 : "+file.getAbsolutePath());
                file.delete();

            }
        }
    }
}
