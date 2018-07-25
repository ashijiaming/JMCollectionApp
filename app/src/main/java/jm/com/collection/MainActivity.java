package jm.com.collection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jm.com.collection.ffmpeg.NdkProjectActivity;
import jm.com.collection.net.LdNetTestActivity;
import jm.com.collection.view.CustomViewActivity;

public class MainActivity extends AppCompatActivity {


    @InjectView(R.id.btn_ld_net)
    Button btnLdNet;
    @InjectView(R.id.sample_text)
    TextView sampleText;
    @InjectView(R.id.btn_custom_view)
    Button btnCustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

    }

    @OnClick({R.id.btn_ld_net, R.id.btn_custom_view,R.id.btn_ndk_project})
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
        }
    }


}
