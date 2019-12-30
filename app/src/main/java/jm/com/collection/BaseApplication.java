package jm.com.collection;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

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
        MultiDex.install(this);


        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);


    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
