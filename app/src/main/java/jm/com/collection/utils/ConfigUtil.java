package jm.com.collection.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by ShiJiaMing on 2019/12/20
 * Description
 **/
public class ConfigUtil {

    public static final String SAVE_PATH = Environment.getExternalStorageDirectory()
            .getPath()+ File.separator+"JMCollectionApp";

    /**
     * 临时文件
     * @return
     */
    public static String createTempFile(){
        StringBuilder builder = new StringBuilder();
        builder.append(SAVE_PATH).append(File.separator).append("temp");
        String path = builder.toString();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 删除临时文件
     * @return
     */
    public static String deleteTempFile(){
        StringBuilder builder = new StringBuilder();
        builder.append(SAVE_PATH).append(File.separator).append("temp");
        String path = builder.toString();
        File file = new File(path);
        deleteDirWithFile(file);
        return path;
    }

    //删除文件及文件夹
    public static void deleteDirWithFile(File dir)
    {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWithFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }


    /**
     * 根目录文件
     * @return
     */
    public static String createRootFile(){
        File file = new File(SAVE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        return SAVE_PATH;
    }
}
