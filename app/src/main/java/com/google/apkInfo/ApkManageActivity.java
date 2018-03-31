package com.google.apkInfo;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.google.delsdcardapk.R;
import com.google.delsdcardapk.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/3/29.
 */

public class ApkManageActivity extends ListActivity {


    private List<ApplicationInfo> appList = null;
    private PackageManager packageManager = null;
    private AppInfoArrayAdapter arrayAdapter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.apk_manage_layout);
        Utils.showLog(this.getLocalClassName()+" : onCreate ");
        packageManager = getPackageManager();
        new LoadAppAsyncTask().execute();


    }

    private List<ApplicationInfo> checkLunchIntentApp(List<ApplicationInfo> apkInfoList){
        ArrayList<ApplicationInfo> appList = new ArrayList<>();
        for (ApplicationInfo appInfo :
                apkInfoList) {
            try{
                if (null != packageManager.getLaunchIntentForPackage(appInfo.packageName)){
                    appList.add(appInfo);
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
            progressDialog.setTitle("加载应用信息中.....");
            progressDialog.show();
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Object o) {
            Utils.showLog(this.getClass().getName()+" : onPostExecute ");
            setListAdapter(arrayAdapter);
            progressDialog.dismiss();
            super.onPostExecute(o);
        }
    }
}
