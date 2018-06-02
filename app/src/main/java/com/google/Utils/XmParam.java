package com.google.Utils;

import android.os.Environment;

public class XmParam {
    /**
     * 这个类用来放各种静态的变量,和全局变量
     *
     */
    public static String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String debugFileName = "isDebugMode";
    public static String debugFileName_debug_id = "isDebugMode_id";
    public static String debugFileName_debug_showLog = "isDebugMode_showLog";

}
