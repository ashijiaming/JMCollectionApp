package jm.com.collection.mqtt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.OnClick;
import jm.com.collection.R;

public class MQTTActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);
        startService(new Intent(this,MQTTService.class));

    }
     @OnClick(R.id.btn_send_message)
     public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_send_message:
                MQTTService.publish("hello php");
                break;
        }
     }

}
