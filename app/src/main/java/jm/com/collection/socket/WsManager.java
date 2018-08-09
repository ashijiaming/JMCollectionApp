package jm.com.collection.socket;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 创建时间: 2018/4/12
 * 创建人: dell
 * 描述: //TODO
 * 版本: V1.0
 */

public class WsManager {
    private static final String TAG = WsManager.class.getSimpleName();
    private static String DEF_URL;
    /**
     * WebScoket config
     */
    private static final int FRAME_QUEUE_SIZE = 5;
    private static final int CONNECT_TIMEOUT = 5000;


    private int reconnectCount = 0;//重连次数
    private long minInterval = 3000;//重连最小时间间隔
    private int maxInterval = 60000;//重连最大时间间隔

    /**
     * 其实获取连接地址这个地方是可以优化的,就是app启动的时候先比较
     * 上次获取的时间如果大于6小时就通过http请求获取websocket的连接
     * 地址,这个地址应该是个列表,然后存入本地,连接的时候我们可以先
     * ping下地址,选择耗时最短的地址接入.如果连不上我们在连耗时第二
     * 短的地址以此类推.但这里我们就以简单的方式做了.
     */

    /**
     * 管理WebSocket实例的单例对象
     */
    private static WsManager sManager;
    /**
     * WebSocket实例
     */
    private WebSocket mWebSocket;
    /**
     * 地址
     */
    private String mUrl;
    /**
     * 连接状态
     */
    private WsStatus mWsStatus;
    /**
     * 连接服务的监听
     */
    private WsListener mWebsocketListener;

    /**
     * 接收服务消息的监听
     */
    private FromServerListener mFromServerListener;
    /**
     * 连接状态的监听
     */
    private UpStatusListener mUpStatusListener;


    private Handler mReconnectHandler = new Handler();

    /*心跳配置*/
    private static final long HEARTBEAT_INTERVAL = 23000;//心跳间隔
    private Handler mHeartHandler = new Handler();
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private HashMap<String, ScheduledFuture> heartTimeOutMap = new HashMap<>();
    private int heartBeatFailCount = 0;

    private WsManager() {
    }

    public static WsManager getInstance() {
        if (sManager == null) {
            synchronized (WsManager.class) {
                if (sManager == null) {
                    sManager = new WsManager();
                }
            }
        }
        return sManager;
    }

    public void init() {
        Log.i(TAG, "init: ");
        try {/**
         * configUrl其实是缓存在本地的连接地址
         * 这个缓存本地连接地址是app启动的时候通过http请求去服务端获取的,
         * 每次app启动的时候会拿当前时间与缓存时间比较,超过6小时就再次去服务端获取新的连接地址更新本地缓存
         */
            DEF_URL = SocketConfig.SOCKET_URL;
            String configUrl = "";
            String account = SocketConfig.ACCOUNT;
            String password = SocketConfig.PASSWORD;
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                setStatus(WsStatus.ACCOUNT_PASSWORD_ERROR,"找不到用户名或者密码！");
                return;
            }
            mUrl = TextUtils.isEmpty(configUrl) ? DEF_URL : configUrl;
            mWebsocketListener = new WsListener();
            mWebSocket = new WebSocketFactory().createSocket(mUrl, CONNECT_TIMEOUT)
                    .setFrameQueueSize(FRAME_QUEUE_SIZE)//设置帧队列最大值为5
                    .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                    .addListener(mWebsocketListener)//添加回调
                    .addHeader("username", account)
                    .addHeader("password", password)
                    .connectAsynchronously();//异步连接
            setStatus(WsStatus.CONNECTING,"第一次连接");
            Log.i(TAG, "第一次连接");
        } catch (IOException e) {
            Log.i(TAG, "出错了 " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 继承默认的监听空实现WebSocketAdapter,重写我们需要的方法
     * onTextMessage 收到文字信息
     * onConnected 连接成功
     * onConnectError 连接失败
     * onDisconnected 连接关闭
     */
    public class WsListener extends WebSocketAdapter {
        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            super.onTextMessage(websocket, text);
            Log.i(TAG, "onTextMessage: text = " + text);
            if (mFromServerListener != null) {
                mFromServerListener.fromServerText(text);
            }
        }

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            super.onConnected(websocket, headers);
            Log.i(TAG, "onConnected: 连接成功");
            setStatus(WsStatus.CONNECT_SUCCESS,"连接成功");
            startHeartBeat();
            cancelReconnect();
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
            super.onConnectError(websocket, exception);
            Log.i(TAG, "onConnectError: 连接错误 :exception = " + exception);
            setStatus(WsStatus.CONNECT_FAIL,"连接错误");
            reconnect();
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            Log.i(TAG, "onDisconnected: 断开连接");
            setStatus(WsStatus.CONNECT_FAIL,"断开连接");
            reconnect();
        }

        @Override
        public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            super.onPongFrame(websocket, frame);
            String payloadText = frame.getPayloadText();
            Log.i("heartBeat", "onPongFrame: payloadText = " + payloadText);
            if (payloadText != null) {
                ScheduledFuture scheduledFuture = heartTimeOutMap.remove(payloadText);
                if (scheduledFuture == null) {
                    Log.i("heartBeat", String.format("onPongFrame: 没有找到key%s ", payloadText));
                    return;
                }

                try {
                    scheduledFuture.cancel(true);
                    heartBeatFailCount = 0;
                } catch (Exception e) {
                    heartBeatFailCount++;
                    if (heartBeatFailCount > 3) {
                        reconnect();
                    }
                }

            } else {
                heartBeatFailCount++;
                if (heartBeatFailCount > 3) {
                    reconnect();
                }
            }
        }
    }

//    private void setStatus(WsStatus wsStatus) {
//        mWsStatus = wsStatus;
//        if (mUpStatusListener != null) {
//            mUpStatusListener.upStatus(wsStatus);
//        }
//    }

    private void setStatus(WsStatus wsStatus,String desc) {
        mWsStatus = wsStatus;
        if (mUpStatusListener != null) {
            mUpStatusListener.upStatus(wsStatus,desc);
        }
    }

    public WsStatus getWsStatus() {
        return mWsStatus;
    }

    public void disConnect() {
        if (mWebSocket != null)
            mWebSocket.disconnect();
    }

    public void reconnect() {
        if (mWebSocket != null &&
                !mWebSocket.isOpen() && //当前连接断开了
                getWsStatus() != WsStatus.CONNECTING) {//不是正在连接状态
            reconnectCount++;

            long reconnectTime = minInterval;
            if (reconnectCount > 3) {
                mUrl = DEF_URL;
                long temp = minInterval * (reconnectCount - 2);
                reconnectTime = temp > maxInterval ? maxInterval : temp;
            }

            cancelHeartBeat();//取消心跳
            setStatus(WsStatus.CONNECTING,String.format("开始第%d重连",reconnectCount));
            Log.i(TAG, String.format("reconnect: 准备开始第%d重连，重连间隔%d -- url:%s", reconnectCount, reconnectTime, mUrl));
//            mReconnectHandler.removeCallbacks(mReconnectTask);
            mReconnectHandler.postDelayed(mReconnectTask, reconnectTime);
        }
    }

    private Runnable mReconnectTask = new Runnable() {
        @Override
        public void run() {
            try {
                String account = SocketConfig.ACCOUNT;
                String password = SocketConfig.PASSWORD;
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                    setStatus(WsStatus.ACCOUNT_PASSWORD_ERROR,"找不到用户名或者密码！");
                    return;
                }
                mWebSocket = new WebSocketFactory().createSocket(mUrl, CONNECT_TIMEOUT)
                        .setFrameQueueSize(FRAME_QUEUE_SIZE)//设置帧队列最大值为5
                        .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                        .addListener(mWebsocketListener = new WsListener())//添加回调监听
                        .addHeader("username", account)
                        .addHeader("password", password)
                        .connectAsynchronously();//异步连接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public void cancelReconnect() {
        reconnectCount = 0;
        mReconnectHandler.removeCallbacks(mReconnectTask);
    }



    /*分割线    心跳*/
    private void startHeartBeat() {
        mHeartHandler.postDelayed(heartBeatTask, HEARTBEAT_INTERVAL);
    }

    private void cancelHeartBeat() {
        heartBeatFailCount = 0;
        mHeartHandler.removeCallbacks(heartBeatTask);
    }

    private Runnable heartBeatTask = new Runnable() {
        @Override
        public void run() {
            String lastPing = String.valueOf(System.currentTimeMillis());
            ScheduledFuture scheduledFuture = enqueueTimeOut(lastPing, 10000);
            heartTimeOutMap.put(lastPing, scheduledFuture);
            mWebSocket.sendPing(lastPing);
            mHeartHandler.postDelayed(this, HEARTBEAT_INTERVAL);
            Log.i("heartBeat", "心跳 run: lastPing = " + lastPing);
        }
    };

    private ScheduledFuture enqueueTimeOut(final String lastPing, int timeOut) {
        ScheduledFuture schedule = executor.schedule(new Runnable() {
            @Override
            public void run() {
                ScheduledFuture scheduledFuture = heartTimeOutMap.remove(lastPing);
                if (scheduledFuture == null) {
                    return;
                }
                heartBeatFailCount++;
                if (heartBeatFailCount > 3) {
                    reconnect();
                }
            }
        }, timeOut, TimeUnit.MILLISECONDS);
        return schedule;
    }

    /**
     * 业务相关
     */
    public boolean sendText(String string) {
        if (mWsStatus == WsStatus.CONNECT_SUCCESS) {
            mWebSocket.sendText(string);
            return true;
        } else {
            return false;
        }
    }

    public void removeFromServerListener() {
        mFromServerListener = null;
    }

    public void setFromServerListener(FromServerListener fromServerListener) {
        mFromServerListener = fromServerListener;
    }

    public interface FromServerListener {
        void fromServerText(String string);
    }

    public interface UpStatusListener {
        void upStatus(WsStatus wsStatus, String desc);
    }

    public void removeUpStatusListener() {
        mUpStatusListener = null;
    }

    public void setUpStatusListener(UpStatusListener upStatusListener) {
        mUpStatusListener = upStatusListener;
    }

    public enum WsStatus {
        CONNECTING,//正在连接
        CONNECT_SUCCESS,//连接成功
        CONNECT_FAIL,//连接失败
        ACCOUNT_PASSWORD_ERROR//找不到用户名或者密码！
    }
}
