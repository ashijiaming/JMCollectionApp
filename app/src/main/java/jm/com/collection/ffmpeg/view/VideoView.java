package jm.com.collection.ffmpeg.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2017/9/19.
 * Author Name ShiJiaMing
 * Description :
 */

public class VideoView extends SurfaceView{
    public VideoView(Context context) {
        this(context,null);
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化，SurfaceView绘制的像素格式
        SurfaceHolder holder=getHolder();
        holder.setFormat(PixelFormat.RGBA_8888);
    }
}
