package jm.com.collection.socket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.net.URI;
import java.net.URISyntaxException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jm.com.collection.R;

public class SocketActivity extends AppCompatActivity {

    private static final String TAG="SocketActivity";

    @InjectView(R.id.et_send_content)
    EditText et_send_content;

    ExampleClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_connection_one,R.id.btn_send_message})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.btn_connection_one:
                try {
                    client = new ExampleClient( new URI( SocketConfig.SOCKET_URL ));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                client.connect();
                break;
            case R.id.btn_send_message:
                String content = et_send_content.getText().toString();
                if (content.equals("")){
                    Log.i(TAG,"发送的内容为空");
                    break;
                }
                if (client==null){
                    Log.i(TAG,"socket未连接");
                    break;
                }
                client.send(content);
                break;
        }
    }
}
