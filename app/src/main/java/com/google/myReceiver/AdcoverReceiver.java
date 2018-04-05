package com.google.myReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.delsdcardapk.ButtonUtils;
import com.google.delsdcardapk.Utils;

/**
 * Created by admin on 2018/1/14.
 */

public class AdcoverReceiver extends BroadcastReceiver {





    /**
     *  用来检测覆盖广告是否被执行了
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String msg = intent.getStringExtra("msg");
        if (action.equals("com.google.adCoverMsg")){
            Utils.showLog("相关广告被调用");

            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }else if (action.equals("com.google.isOurGame")){
            Utils.showLog("启动广告游戏");
            ButtonUtils.delAdFlag();
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }else if (action.equals("com.google.adMsg")){
            Utils.showLog("广告被展示了,返回信息为 : "+msg);

//            ButtonUtils.delAdFlag();
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }else if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")||
                action.equals(Intent.ACTION_USER_PRESENT)||
                action.equals(Intent.ACTION_SCREEN_ON)||
                action.equals(Intent.ACTION_SCREEN_OFF)){
            Utils.showLog("乱七八糟的广播");
            msg = "唤醒软件";
        }else if (action.equals(Intent.ACTION_BOOT_COMPLETED) ){
            Toast.makeText(context, "开机启动", Toast.LENGTH_SHORT).show();
            Utils.showLog("开机启动");
            msg = "开机启动";
        }

        Utils.sendReceiver(context,"com.google.showLog",msg);

    }
}
