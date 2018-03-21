package com.google.myService;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.delsdcardapk.Utils;

/**
 * Created by admin on 2018/2/3.
 */

public class ForeverService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        Utils.showLog("后台服务被启动....");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.showLog("后台服务被关闭....");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }


    private class MyBinder extends Binder implements IMyBinder{

        public MyBinder(){
            super();
        }


    }


}
