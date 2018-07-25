package jm.com.collection.ffmpeg;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import java.io.File;

import jm.com.collection.R;
import jm.com.collection.ffmpeg.jni.ThreadVideo;
import jm.com.collection.ffmpeg.view.VideoView;

public class VideoPlayActivity extends AppCompatActivity {

    private Spinner spinner;
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        spinner= (Spinner) findViewById(R.id.spinner_view);
        Button playButton= (Button) findViewById(R.id.btn_play);
        videoView= (VideoView) findViewById(R.id.video_view);

        final String[] videoArray=getResources().getStringArray(R.array.video_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, videoArray);
        spinner.setAdapter(adapter);

        //final DecodeVideo video = new DecodeVideo();
        final ThreadVideo threadVideo = new ThreadVideo();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = spinner.getSelectedItem().toString();
                String path = new File(Environment.getExternalStorageDirectory(), name).getAbsolutePath();
                Surface surface = videoView.getHolder().getSurface();
                //video.render(path,surface);
                threadVideo.decodeVideo(path,surface);
            }
        });
    }
}
