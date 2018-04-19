package com.google.apkInfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.delsdcardapk.R;
import com.google.delsdcardapk.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/3/29.
 */

public class ApkManageActivity extends Activity implements View.OnClickListener{


    private static final int UNSTALL_APK = 0;
    private List<ApplicationInfo> appList = null;
    private PackageManager packageManager = null;
    private AppInfoArrayAdapter arrayAdapter = null;
    private ListView lv_apkInfo = null;
    private ImageView iv_back = null;
    private ApkManagerReceiver apkManagerReceiver = null;
    private AlertDialog.Builder selFunDialog = null;
    private Context mContext = null;
    private int appIndex = 0;   // ListView 中被长按的应用的位置
    private int appListClickCount = 0;   // ListView 中的项目被点击的次数,两次后清零



    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UNSTALL_APK :

                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apk_manage_layout);
        Utils.showLog(this.getLocalClassName()+" : onCreate ");

        initEveryThing();
        initSimpleThing();
    }

    private void initSimpleThing() {
        iv_back = findViewById(R.id.apk_manage_back);
        iv_back.setOnClickListener(this);
    }

    private void initEveryThing() {
        mContext = this;
        packageManager = getPackageManager();
        lv_apkInfo = findViewById(R.id.appInfo_lv);

        new LoadAppAsyncTask().execute();

        /**********动态注册receiver************/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        apkManagerReceiver = new ApkManagerReceiver();
        registerReceiver(apkManagerReceiver,intentFilter);

        lv_apkInfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                appIndex = i;
                selFunDialog.show();
                return true;
            }
        });

        lv_apkInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 判断是不是瞬间双击
                if (Utils.hasAccessTime(500)){
                    appListClickCount = 0;
                }
                appListClickCount++;
                // 判断是不是双击
                if (appListClickCount >= 2){
                    appListClickCount = 0;
                    appIndex = i;
                    selFunDialog.show();
                    // 将时间间隔置零
                    Utils.hasAccessTime(0);
                }
            }
        });
        showFunDialog();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.apk_manage_back:
                finish();
                break;
        }
    }


    private void showFunDialog(){
        selFunDialog = new AlertDialog.Builder(this).setTitle("选择操作:").
                setIcon(R.mipmap.ic_launcher).setItems(
                new String[]{"清除数据","卸载应用","启动应用"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                // 跳转到已安装应用列表
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", appList.get(appIndex).packageName, null);
                                intent.setData(uri);
                                startActivity(intent);
                                break;
                            case 1:
                                // 卸载应用
                                Utils.uninstall_apk(mContext,appList.get(appIndex).packageName);
                                break;
                            case 2:
                                // 启动应用
                                Utils.startApp(mContext,appList.get(appIndex).packageName);
                                break;
                        }

                    }
                });
    }


    /**
     * 这个方法不能用
     * @param packageName 要清除数据的应用的包名
     */
    private void clearUserData(String packageName){
        try {

            // 获取其他应用的上下文
            Context c = createPackageContext(packageName,
                    Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);

            ActivityManager am = (ActivityManager)
                    c.getSystemService(Context.ACTIVITY_SERVICE);
            // 清除对应应用的数据 需要 这个权限(这个权限是系统应用才能有的)"android.permission.CLEAR_APP_USER_DATA"
            am.clearApplicationUserData();
        }  catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取
     * @param apkInfoList
     * @return
     */
    private List<ApplicationInfo> checkLunchIntentApp(List<ApplicationInfo> apkInfoList){
        ArrayList<ApplicationInfo> appList = new ArrayList<>();


        for (ApplicationInfo appInfo :
                apkInfoList) {
            try{
                if (null != packageManager.getLaunchIntentForPackage(appInfo.packageName)){
                    if (appList.size()>0){
                        PackageInfo packageInfo = packageManager.getPackageInfo(
                                appInfo.packageName, 0);
                        /**********对应用安装时间进行排序**************/
                        for (int i = 0; i < appList.size(); i++) {
                            PackageInfo appListPacageInfo = packageManager.getPackageInfo(
                                    appList.get(i).packageName, 0);
                            if (packageInfo.lastUpdateTime >appListPacageInfo.lastUpdateTime ){
                                appList.add(i,appInfo);
                                break;
                            }else if (i == appList.size() -1 ){
                                appList.add(appInfo);
                                break;
                            }
                        }
                    }else {
                        appList.add(appInfo);
                    }


                }
            }catch (Exception e){
                Utils.showLog("checkLunchIntentApp 抛出的warning 异常 , 不用管~~~~");
                e.printStackTrace();
            }

        }

        return appList;
        
        
    }



    /***********异步加载应用信息***********/
    private class LoadAppAsyncTask extends AsyncTask {


        private ProgressDialog progressDialog = null;

        @Override
        protected Object doInBackground(Object[] objects) {
            Utils.showLog(this.getClass().getName()+" : doInBackground ");
            appList = checkLunchIntentApp(packageManager.getInstalledApplications(
                    PackageManager.GET_META_DATA));
            arrayAdapter = new AppInfoArrayAdapter(ApkManageActivity.this,
                    R.layout.list_app_adapter,appList);
            return null;
        }

        @Override
        protected void onPreExecute() {
            Utils.showLog(this.getClass().getName()+" : onPreExecute ");
            progressDialog = new ProgressDialog(ApkManageActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("加载应用信息中.....");
//            progressDialog.setTitle("加载应用信息中.....");
            progressDialog.show();
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Object o) {
            Utils.showLog(this.getClass().getName()+" : onPostExecute ");
            progressDialog.dismiss();
            lv_apkInfo.setAdapter(arrayAdapter);

            super.onPostExecute(o);
        }
    }

    /*******查找 packageName 对应的应用在 appList 中的位置**********/
    public int findAppListPosition(String packageName){

        for (int i = 0; i < appList.size(); i++) {
            ApplicationInfo appInfo = appList.get(i);
            if (appInfo.packageName.equals(packageName)){
                return i;
            }
        }

        return -1;
    }

    private class ApkManagerReceiver extends BroadcastReceiver{



        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String msg = "";
            if (action.equals(Intent.ACTION_PACKAGE_REMOVED)){
//                if (1 != intent.getIntExtra("myUninstall",0)){
//                    return;
//                }

                String packageName = intent.getDataString();
                packageName = packageName.split("package:")[1];
                msg = packageName + " : 被卸载了";
                Utils.showLog(msg);
                int appListPosition = findAppListPosition(packageName);

                if ( appListPosition != -1){
                    //  刷新listView
                    appList.remove(appListPosition);
                    arrayAdapter.notifyDataSetChanged();
                }

            }

            Utils.sendReceiver(context,"com.google.showLog",msg);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(apkManagerReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

    }
}
