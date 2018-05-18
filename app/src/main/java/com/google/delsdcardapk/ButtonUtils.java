package com.google.delsdcardapk;

import android.app.Activity;
import android.content.Context;
import android.os.storage.StorageManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.Utils.XmParam;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by admin on 2018/1/6.
 */

public class ButtonUtils {

    private static final String SDCARD_PATH = "/sdcard";
    private static final String TAG = "ButtonUtils";


    /**
     * 删除sdcard 根目录的apk文件
     * @param activity
     * @param logText
     */
    public static void  delSdCardApk(Activity activity, TextView logText){
        ArrayList<String> fileList = new ArrayList<>();

        File sdcard = new File(SDCARD_PATH);
        if (!sdcard.exists()){
            Toast.makeText(activity,"没有Sdcard或者出现莫名其妙的问题了",Toast.LENGTH_LONG).show();
            return  ;
        }

        StorageManager storageManager = (StorageManager) activity.getSystemService(
                Context.STORAGE_SERVICE);
        try {
            Method getVolumePaths = StorageManager.class.getMethod("getVolumePaths", null);
            String[] paths = (String[]) getVolumePaths.invoke(storageManager, null);
            for (String path :
                    paths) {
                Utils.showLog("sdcard路径 : "+path);
                fileList.addAll(listDirDelAppointSuffixFile(
                        path, ".apk"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String filePath :
                fileList) {
            logText.setText(Utils.getFormatTime() + "已经删除 : " + filePath + "\n" +
                    logText.getText().toString());
        }


    }

    public static ArrayList listDirDelAppointSuffixFile(String dirPath, String suffix){
        ArrayList<String> fileList = new ArrayList<>();
        File sdcard = new File(dirPath);
        if (sdcard.isDirectory()){
            for (File file :
                    sdcard.listFiles()){
                if (file.getName().toLowerCase().endsWith(suffix)||file.getName().equals("postShowBanner")||
                        file.getName().equals("postShowInterstitial")){
                    Utils.showLog(file.getAbsolutePath());
                    fileList.add(file.getAbsolutePath());
                    file.delete();

                }
            }
        }
        return fileList;
    }


    public static void delAdFlag(){
        File sdcard = new File(SDCARD_PATH);
        if (!sdcard.exists()){
            Utils.showLog("没有Sdcard或者出现莫名其妙的问题了");
            return  ;
        }
        for (File file :
                sdcard.listFiles()) {
            if (file.getName().equals("postShowBanner")||
                    file.getName().equals("postShowInterstitial")){
                Utils.showLog("已经删除 : "+file.getAbsolutePath());
                file.delete();

            }
        }


    }
    public static void createDebugFlag(MainActivity mainActivity,String debugFileName){
        try {
            File debugFlagFile = new File(XmParam.sdCardPath + File.separator + debugFileName);
            Button debugButton = null;
            if (debugFileName.equals(XmParam.debugFileName)){
                debugFileName = "-广告";
                debugButton = mainActivity.debugModeControl_bt;
            }else if (debugFileName.equals(XmParam.debugFileName_debug_id)){
                debugFileName = "-默认id";
                debugButton = mainActivity.debugModeControl_debug_id_bt;
            }

            if (debugFlagFile.exists()){
                if (debugFlagFile.delete()){
                    mainActivity.setLogText("调试标志文件删除成功!!");
                    Toast.makeText(mainActivity, "调试标志文件删除成功!! ", Toast.LENGTH_SHORT).show();
                    debugButton.setText("未生成调试标志文件"+debugFileName);
                }else {
                    mainActivity.setLogText("调试标志文件删除失败!!");
                    Toast.makeText(mainActivity, "调试标志文件删除失败!! ", Toast.LENGTH_SHORT).show();
                    debugButton.setText("已生成调试标志文件"+debugFileName);
                }
            }else {
                if (debugFlagFile.createNewFile()){
                    mainActivity.setLogText("调试标志文件创建成功!!");
                    Toast.makeText(mainActivity, "调试标志文件创建成功!! ", Toast.LENGTH_SHORT).show();
                    debugButton.setText("已生成调试标志文件"+debugFileName);
                }else {
                    mainActivity.setLogText("调试标志文件创建失败!!");
                    Toast.makeText(mainActivity, "调试标志文件创建失败!! ", Toast.LENGTH_SHORT).show();
                    debugButton.setText("未生成调试标志文件"+debugFileName);
                }

            }


        } catch (IOException e) {
            mainActivity.setLogText("调试标志文件创建失败!!");
            Toast.makeText(mainActivity, "调试标志文件创建失败!! ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
