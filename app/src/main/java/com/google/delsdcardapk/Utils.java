package com.google.delsdcardapk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2018/1/6.
 */

public class Utils {

    private static final boolean ISDEBUG = true;
    private static final String TAG = "delSdcardApk_xyz";

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

    public static int checkAdHasShowed(Context context){

        try {
            if (new File("/sdcard/postShowInterstitial").exists()){
                Toast.makeText(context, "插屏广告被执行了", Toast.LENGTH_SHORT).show();
                return 0;
            }else if (new File("/sdcard/postShowBanner").exists()){
                Toast.makeText(context, "横幅广告被执行了", Toast.LENGTH_SHORT).show();
                return 1;
            }
            return -1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * 格式化单钱时间并且返回
     * @return
     */
    public static String getFormatTime(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }


    public static void sendReceiver(Context context,String action,String msg){
        Intent intent = new Intent(action);
        intent.putExtra("msg",msg);
        context.sendBroadcast(intent);
    }
}
