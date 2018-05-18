package com.google.Utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    // 文件是否存在
    public static boolean existFile(String filePath){
        return new File(filePath).exists();
    }

    // 创建新文件
    public static boolean createFile(String filePath){
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // 删除文件 ,可以删除文件夹
    public static boolean deleteFile(String filePath){
        File file = new File(filePath);
        boolean success = false;
        if (file.isDirectory()){
            for (File subFile :
                    file.listFiles()) {
                success = deleteFile(subFile.getAbsolutePath());
                if (!success){
                    return success;
                }
            }
            return success;
        }
        try{
            file.delete();
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
        return true;
    }
}
