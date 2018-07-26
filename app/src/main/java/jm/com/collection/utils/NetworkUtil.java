package jm.com.collection.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import jm.com.collection.BaseApplication;

public class NetworkUtil {

    /**
     * 判断网络是否可用
     * @return
     */
    private boolean isNetConnect() {
        ConnectivityManager connectManager = (ConnectivityManager) BaseApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectManager != null) {
            NetworkInfo info = connectManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
