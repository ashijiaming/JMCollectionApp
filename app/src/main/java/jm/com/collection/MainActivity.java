package jm.com.collection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jm.com.collection.activity.AIActivity;
import jm.com.collection.activity.HtmlActivity;
import jm.com.collection.activity.JavaWebActivity;
import jm.com.collection.adapter.MainAdapter;
import jm.com.collection.ffmpeg.NdkProjectActivity;
import jm.com.collection.mqtt.MQTTActivity;
import jm.com.collection.net.LdNetTestActivity;
import jm.com.collection.socket.SocketActivity;
import jm.com.collection.view.CustomViewActivity;
import jm.com.collection.webview.X5WebViewActivity;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.rv_main_list)
    RecyclerView rv_main_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        List<String> list=new ArrayList<>();
        requestMustPermission(this);
        list.add("LD网络框架测试");
        list.add("自定义View");
        list.add("FFMEPG学习");
        list.add("Socket研究");
        list.add("MQTT研究");
        list.add("原生与Web交互");
        list.add("百度AI");
        list.add("X5内核浏览器");
        list.add("Java后台");

        final List<Class> classList=new ArrayList<>();
        classList.add(LdNetTestActivity.class);
        classList.add(CustomViewActivity.class);
        classList.add(NdkProjectActivity.class);
        classList.add(SocketActivity.class);
        classList.add(MQTTActivity.class);
        classList.add(HtmlActivity.class);
        classList.add(AIActivity.class);
        classList.add(X5WebViewActivity.class);
        classList.add(JavaWebActivity.class);


        MainAdapter mainAdapter = new MainAdapter(this, list);
        rv_main_list.setAdapter(mainAdapter);
        mainAdapter.setItemOnclickListener(new MainAdapter.ItemOnclickListener() {
            @Override
            public void onClick(int position) {
                startActivity(new Intent(MainActivity.this, classList.get(position)));
            }
        });
    }

    /**
     * 必须权限授予首页
     */
    private void requestMustPermission(Context context) {
        AtomicInteger count = new AtomicInteger();
        AtomicBoolean granted = new AtomicBoolean(true);
        String[] manifestPermissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO",
                "android.permission.CAMERA", "android.permission.READ_PHONE_STATE"};
        RxPermissions.getInstance(context)
                .requestEach(manifestPermissions)
                .subscribe(permission -> {
                    count.getAndIncrement();
                    if (permission.granted) {
                    } else {
                        granted.set(false);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
