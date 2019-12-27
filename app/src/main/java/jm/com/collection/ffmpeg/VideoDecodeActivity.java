package jm.com.collection.ffmpeg;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jm.ffmpeg.jni.DecodeVideo;

import jm.com.collection.R;


public class VideoDecodeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="VideoDecodeActivity";
    private  Button btnDecodeVideo;
    private Button btnDecodeAudio;
    private DecodeVideo video;
    private TextView tvProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_decode);
        btnDecodeVideo= (Button) findViewById(R.id.btn_decode_video);
        btnDecodeAudio=(Button) findViewById(R.id.btn_decode_audio);
        findViewById(R.id.btn_decode_audio);
        tvProgress= (TextView) findViewById(R.id.tv_progress);
        btnDecodeVideo.setOnClickListener(this);
        btnDecodeAudio.setOnClickListener(this);
        video = new DecodeVideo();
        video.setDecodeProgressListener(new DecodeVideo.DecodeProgressListener() {
            @Override
            public void currentProgress(int progress) {
                Log.i(TAG,"progress"+progress+"  "+Thread.currentThread().getName());
                tvProgress.setText("解码成功，共"+progress+" 帧");
            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_decode_video) {
            String inputPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/input.mp4";
            String outputPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/output.yuv";
            String decode = video.decode(inputPath,outputPath);
            Toast.makeText(VideoDecodeActivity.this,decode,Toast.LENGTH_SHORT).show();
        }else if (i==R.id.btn_decode_audio){
            String inputPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ggok.mp3";
            String outputPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/output.pcm";
            video.sound(inputPath,outputPath);
        }
    }

}
