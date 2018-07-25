package jm.com.collection.view.me;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2017/8/19.
 * Author Name ShiJiaMing
 * Description : 通过赛贝尔曲线绘制水波纹，也是WaveDrawable的原理
 */

public class WaveView  extends View{

    public final String TAG="WaveView";

    private  Paint mPaint;
    private  Path mPath;
    private int mScreenHeight;
    private int mScreenWidth;
    //一个周期的长度
    private int mWaveLength;
    //最高点的高度
    private int mWaveHeight;

    private int mWaveCount;
    private int mCenterY;
    private int mOffset;
    private ValueAnimator valueAnimator;

    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
         mPath = new Path();
         mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
         mPaint.setColor(Color.LTGRAY);
         mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mWaveLength=1000;
        mWaveHeight=100;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenHeight=h;
        mScreenWidth=w;
        //至少保证有两个波纹才能够进行平移
         mWaveCount =  Math.round(mScreenWidth / mWaveLength + 2);
         mCenterY=mScreenHeight/2;

        Log.d(TAG, "mWaveCount: "+mWaveCount+"mScreenHeight:"+mScreenHeight+"mScreenWidth:"+mScreenWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        //将绘制的起始点移动到屏幕最外面
        mPath.moveTo(-mWaveLength+mOffset,mCenterY);
        //绘制每条波纹，总共有mWaveCount条
        for (int i=0;i<mWaveCount;i++){
            //前半段
            mPath.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY + mWaveHeight, (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
            //后半段
            mPath.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY - mWaveHeight, i * mWaveLength + mOffset, mCenterY);
        }

        mPath.lineTo(mScreenWidth,mScreenHeight);
        mPath.lineTo(0,mScreenHeight);
//         mPath.close();
        canvas.drawPath(mPath,mPaint);
    }


    public void startAnimator() {
        valueAnimator=ValueAnimator.ofInt(0,mWaveLength);
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset=(int)animation.getAnimatedValue();
                Log.i("Offset","偏移量"+mOffset);
                postInvalidate();
            }
        });
        valueAnimator.start();

    }
    public void stopAnimator(){
        if (valueAnimator!=null&&valueAnimator.isRunning()){
            valueAnimator.removeAllUpdateListeners();
        }
    }

}
