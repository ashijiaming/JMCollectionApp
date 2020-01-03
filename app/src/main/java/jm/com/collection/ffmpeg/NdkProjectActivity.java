package jm.com.collection.ffmpeg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jm.media.util.LogInfo;

import java.util.ArrayList;
import java.util.List;

import jm.com.collection.R;

/**
 * Created by Administrator on 2017/9/13.
 * Author Name ShiJiaMing
 * Description :
 */

public class NdkProjectActivity extends AppCompatActivity {


    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndk_main);
        mListView= (ListView) findViewById(R.id.lv_ndk_list);
        List<String> titles = new ArrayList<>();
        final List<Class> activities = new ArrayList<>();
        titles.add("1.视频解码将MP4转换为YUV格式");
        titles.add("2.像素格式转换和native原生绘制");
        titles.add("3.执行FFmpeg命令");
        titles.add("4,视频录制压缩");

        activities.add(VideoDecodeActivity.class);
        activities.add(VideoPlayActivity.class);
        activities.add(ExecuteCommandActivity.class);
        activities.add(RecordVideoActivity.class);

        NdkTitleAdapter adapter = new NdkTitleAdapter(this, R.layout.list_view_item, titles);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener((parent, view, position, id) -> startActivity(new Intent(NdkProjectActivity.this, activities.get(position))));
    }


    public static int getDisplayRotation(Activity activity) {
        if(activity == null)
            return 0;

        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }


    class NdkTitleAdapter extends ArrayAdapter<String> {
        private int mResource;

        public NdkTitleAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.mResource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = getLayoutInflater().inflate(mResource, null);
            TextView title =view.findViewById(R.id.tv_list_title);
            title.setText(getItem(position));
            return view;
        }
    }

}
