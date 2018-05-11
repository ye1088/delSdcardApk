package com.google.delsdcardapk;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.apkInfo.ApkManageActivity;
import com.google.myReceiver.AdcoverReceiver;
import com.google.myService.ForeverService;
import com.google.myService.IMyBinder;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private static final int NO_AD = -1;
    private static final int INTER = 0;
    private static final int BANNER = 1;
    private static final int BIND_SERVICE = 2;
    TextView logText ;
    private MyConn myConn;
    private IMyBinder iMyBinder;
    private ShowLogReceiver showLogReceiver;
    private AdcoverReceiver wakenReceiver;
    private String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private Button debugModeControl_bt = null;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case BIND_SERVICE:
                    Intent foreverService = new Intent(MainActivity.this,
                            ForeverService.class);
                    myConn = new MyConn();
                    bindService(foreverService,myConn,BIND_AUTO_CREATE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.checkPermission(this);
        initView();
        initAnyThing();
    }

    private void initAnyThing() {

        /**********动态注册receiver 开始**************/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.google.showLog");
        showLogReceiver = new ShowLogReceiver();
        registerReceiver(showLogReceiver,intentFilter);
        /**=============================================**/

        wakenReceiver = new AdcoverReceiver();
        IntentFilter wakenFilter = new IntentFilter();
        wakenFilter.addAction(Intent.ACTION_SCREEN_ON);
        wakenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        wakenFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        registerReceiver(wakenReceiver,wakenFilter);

        /**********动态注册receiver 结束**************/
        mHandler.removeMessages(BIND_SERVICE);
        mHandler.sendEmptyMessage(BIND_SERVICE);


        debugModeControl_bt = findViewById(R.id.createDebugFlag);


        if (new File(sdCardPath+File.separator+"isDebugMode").exists()){
            debugModeControl_bt.setText("已生成调试标志文件");
        }


    }

    private class MyConn implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iMyBinder = (IMyBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    private void initView() {
        logText = findViewById(R.id.logMsg);
    }

    public void buttonClick(View view) {

        switch (view.getId()){
            case R.id.delApk:
                ButtonUtils.delSdCardApk(this,logText);
                debugModeControl_bt.setText("未生成调试标志文件");
                break;
            case R.id.go_apkManager:
//                Intent intent = new Intent(this, ApkManageActivity.class);
//                startActivity(intent);
                Utils.gotoNextActivity(this, ApkManageActivity.class);
                break;
            case R.id.checkAd:

                int adState = Utils.checkAdHasShowed(this);
                switch (adState){
                    case NO_AD:
                        setLogText("广告没有展示\n");
                        break;
                    case INTER:
                        setLogText("插屏广告逻辑被执行了\n");
                        break;
                    case BANNER:
                        setLogText("横幅广告逻辑被执行了\n");
                        break;


                }

                break;
            case R.id.clearLog:
//                Toast.makeText(this, "clear log  bei diao yong ", Toast.LENGTH_SHORT).show();
                logText.setText(Utils.getFormatTime()+" : "+"空");
                break;
            case R.id.createDebugFlag:
                try {
                    new File(sdCardPath+File.separator+"isDebugMode").createNewFile();
                    setLogText("调试标志文件创建成功!!");
                    Toast.makeText(this, "调试标志文件创建成功!! ", Toast.LENGTH_SHORT).show();
                    debugModeControl_bt.setText("已生成调试标志文件");
                } catch (IOException e) {
                    setLogText("调试标志文件创建失败!!");
                    Toast.makeText(this, "调试标志文件创建失败!! ", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 展示log信息到textview中去
     * @param msg
     */
    private void setLogText(String msg){
        logText.setText(Utils.getFormatTime()+" : "+msg +"\n"+ logText.getText().toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(permissions,1);
                }
            }
        }
    }


    @Override
    protected void onDestroy() {

        unbindService(myConn);
        unregisterReceiver(showLogReceiver);
        unregisterReceiver(wakenReceiver);
        mHandler.removeMessages(BIND_SERVICE);
        mHandler.sendEmptyMessageDelayed(BIND_SERVICE,1000);
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {

    }

    public class ShowLogReceiver extends BroadcastReceiver{
        /**
         * 用来显示相关的log信息
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String msg = intent.getStringExtra("msg");
            if (action.equals("com.google.showLog")){
                setLogText(msg);
            }
        }
    }
}
