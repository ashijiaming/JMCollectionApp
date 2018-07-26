package jm.com.collection.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * 创建时间: 2018/4/13
 * 创建人: dell
 * 描述: //TODO
 * 版本: V1.0
 */

public class WebSocketService extends Service {
    private static ScheduledExecutorService mScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture mScheduledFuture;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WsManager.getInstance().init();

        WsManager.getInstance().setFromServerListener(new WsManager.FromServerListener() {
            @Override
            public void fromServerText(String string) {
                if (!TextUtils.isEmpty(string)) {
                    Log.i("WsManager", "fromServerText: dataProtocol = " + string);
                    if (string != null) {
                        try {
                        } catch (Exception e) {
                            Log.i("WsManager", "请求太频繁了");
                            e.printStackTrace();
                            String json = "请求太频繁了";
                            WsManager.getInstance().sendText(json);
                        }
                    } else {
                        String json ="数据格式错误";
                        WsManager.getInstance().sendText(json);
                    }
                } else {
                    String json = "请求数据为空";
                    WsManager.getInstance().sendText(json);
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WsManager.getInstance().removeFromServerListener();
        if (WsManager.getInstance().getWsStatus() == WsManager.WsStatus.CONNECT_SUCCESS) {
            WsManager.getInstance().disConnect();
        } else if (WsManager.getInstance().getWsStatus() == WsManager.WsStatus.CONNECT_FAIL) {
            WsManager.getInstance().cancelReconnect();
        }
    }
}
