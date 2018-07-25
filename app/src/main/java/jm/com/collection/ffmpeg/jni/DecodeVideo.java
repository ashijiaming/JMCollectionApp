package jm.com.collection.ffmpeg.jni;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import android.view.Surface;

/**
 * Created by Administrator on 2017/9/14.
 * Author Name ShiJiaMing
 * Description :Native层方法
 */

public class DecodeVideo {
    private static final String TAG="DecodeVideo";
    //解码视频，将MP4转换成YUV
    public native String decode(String input,String output);

    //解码音频文件
    public native void sound(String input,String output);

    //播放各种类型的视频
    public native void render(String input, Surface surface);


    /**
     * 被JNI调用，用于播放音频
     * @param sampleRateInHz
     * @param nb_channels
     * @return
     */
    public AudioTrack createAudioTrack(int sampleRateInHz,int nb_channels){

        Log.i(TAG,"sampleRateInHz: "+sampleRateInHz+"----nb_channels: "+nb_channels);
        //固定格式的音频码流
        int pcm16bit = AudioFormat.ENCODING_PCM_16BIT;
        //声道布局
        int channelConfig;
        if (nb_channels==1){
            channelConfig=AudioFormat.CHANNEL_OUT_MONO;
        }else if (nb_channels==2){
            channelConfig=AudioFormat.CHANNEL_OUT_STEREO;
        }else {
            channelConfig=AudioFormat.CHANNEL_OUT_STEREO;
        }

        int bufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, pcm16bit);
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRateInHz, channelConfig,
                pcm16bit, bufferSize, AudioTrack.MODE_STREAM);

        //audioTrack.play();
        return audioTrack;
    }

    /**
     * 被JNI调用，得到当前最新进度
     * @param progress
     * @return
     */
    public int updateProgress(int progress){
        Log.i("DecodeVideo","我被jin调用了"+progress);
        decodeProgressListener.currentProgress(progress);
        return 0;
    }

    private DecodeProgressListener decodeProgressListener;
    public void setDecodeProgressListener(DecodeProgressListener listener){
        this.decodeProgressListener=listener;
    }
    public interface DecodeProgressListener{
        void currentProgress(int progress);
    }

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("avutil-54");
        System.loadLibrary("swresample-1");
        System.loadLibrary("avcodec-56");
        System.loadLibrary("avformat-56");
        System.loadLibrary("swscale-3");
        System.loadLibrary("postproc-53");
        System.loadLibrary("avfilter-5");
        System.loadLibrary("avdevice-56");
        System.loadLibrary("yuv");
    }
}
