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
        }else if (action.equals("android.intent.action.BOOT_COMPLETED") ||
                action.equals("android.net.conn.CONNECTIVITY_CHANGE")){
            Utils.showLog("开机启动/网络切换广播");
        }

        Utils.sendReceiver(context,"com.google.showLog",msg);

    }
}
