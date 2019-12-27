package jm.com.collection.ffmpeg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jm.media.command.FFmpegPresenter;
import com.jm.media.command.FFmpegUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jm.com.collection.R;
import jm.com.collection.utils.ConfigUtil;

public class ExecuteCommandActivity extends AppCompatActivity {
    private FFmpegPresenter fFmpegPresenter;
    ArrayList<String[]> commandList;
    private static final String TAG ="ExecuteCommandActivity" ;
    private String path,video,slientAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_command);
        ButterKnife.bind(this);
        path = ConfigUtil.createRootFile();
        video=path+File.separator+"complete.mp4";
        slientAudio=path+File.separator+"background.mp3";
        fFmpegPresenter=new FFmpegPresenter(this);
        commandList = new ArrayList<>();
        fFmpegPresenter.setFFmpegListener(new FFmpegPresenter.FFmpegListener() {
            @Override
            public void onAllFinish(int type) {

            }
        });
    }

    @OnClick({R.id.btn_extract_audio,R.id.btn_extract_video})
    public void OnclickEvent(View view){
        switch (view.getId()){
            case R.id.btn_extract_audio:
                commandList.clear();
                String[] strings = FFmpegUtil.extractAudio(video, slientAudio);
                commandList.add(strings);
                fFmpegPresenter.executeFFmpeg(commandList,1);
                break;
            case R.id.btn_extract_video:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
