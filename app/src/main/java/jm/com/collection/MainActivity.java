package jm.com.collection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jm.com.collection.activity.HtmlActivity;
import jm.com.collection.ffmpeg.NdkProjectActivity;
import jm.com.collection.mqtt.MQTTActivity;
import jm.com.collection.net.LdNetTestActivity;
import jm.com.collection.socket.SocketActivity;
import jm.com.collection.view.CustomViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

    }

    @OnClick({R.id.btn_ld_net, R.id.btn_custom_view,R.id.btn_ndk_project,R.id.btn_socket_knowledge,R.id.btn_mqtt_knowledge,
              R.id.btn_web_android})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ld_net:
                startActivity(new Intent(MainActivity.this, LdNetTestActivity.class));
                break;
            case R.id.btn_custom_view:
                startActivity(new Intent(MainActivity.this, CustomViewActivity.class));
                break;
            case R.id.btn_ndk_project:
                startActivity(new Intent(MainActivity.this, NdkProjectActivity.class));
                break;
            case R.id.btn_socket_knowledge:
                startActivity(new Intent(MainActivity.this, SocketActivity.class));
                break;
            case R.id.btn_mqtt_knowledge:
                startActivity(new Intent(MainActivity.this, MQTTActivity.class));
                break;
            case R.id.btn_web_android:
                startActivity(new Intent(MainActivity.this,HtmlActivity.class));
                break;
        }
    }


}
