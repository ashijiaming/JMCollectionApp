package jm.com.collection;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

/**
 * Created by Administrator on 2016/10/20.
 * Author Name ShiJiaMing
 * Description :  自定义Application,很多模块初始化
 */

public class BaseApplication extends Application{

    private static final String  TAG="BaseApplication";
     public static Context sContext;
     public static Resources sResource;
     private static BaseApplication instance;



    //运行系统是否为2.3或以上
     public static boolean isAtLeastGB;
    /**
     * 获得当前app运行的AppConstant
     * @return
     */
    public static BaseApplication getInstance() {
        return instance;
    }
    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            isAtLeastGB = true;
        }
    }

    public static synchronized BaseApplication context() {
        return (BaseApplication) sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sResource = sContext.getResources();
        instance=this;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
