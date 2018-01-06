package com.google.delsdcardapk;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

/**
 * Created by admin on 2018/1/6.
 */

public class Utils {

    private static final boolean ISDEBUG = true;
    private static final String TAG = "delSdcardApk";

    public static void showLog(String msg){
        if (ISDEBUG){
            Log.e(TAG,msg);
        }
    }


    public static void checkPermission(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            activity.requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }
    }
}
