package com.jm.media.record;

/**
 * Created by jianxi on 2017/6/5.
 * https://github.com/mabeijianxi
 * mabeijianxi@gmail.com
 */

public class JianXiCamera {
    /** 应用包名 */
    private static String mPackageName;
    /** 应用版本名称 */
    private static String mAppVersionName;
    /** 应用版本号 */
    private static int mAppVersionCode;
    /** 视频缓存路径 */
    private static String mVideoCachePath;

    /** 获取视频缓存文件夹 */
    public static String getVideoCachePath() {
        return mVideoCachePath;
    }

    /** 设置视频缓存路径 */
    public static void setVideoCachePath(String path) {
        mVideoCachePath = path;
    }
}
