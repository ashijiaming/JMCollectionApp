package com.jm.media.command;

import android.content.Context;

import com.jm.media.command.exceptions.FFmpegCommandAlreadyRunningException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ShiJiaMing on 2019/12/12
 * Description
 **/
public class FFmpegPresenter {
    private static  final  String TAG="FFmpegPresenter";
    private Context mContext;
    private FFmpeg ffmpeg;

    public FFmpegPresenter(Context context){
        this.mContext=context;
        if (ffmpeg==null){
            ffmpeg=FFmpeg.getInstance(context);
            try {
                ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                    @Override
                    public void onFailure() {
                        Log.i("Load Failure");
                    }

                    @Override
                    public void onSuccess() {
                        Log.i("Load Success");
                    }
                });
            } catch (Exception e) {
            }
        }
    }


    public void executeFFmpeg(final List<String[]> list, final int type) {
        final String[] command = list.get(0);
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    super.onStart();
                    Log.i("onStart : ffmpeg" + command);
                }

                public void onProgress(String s) {
                    Log.i("progress :" + s);
                }

                @Override
                public void onSuccess(String message) {
                    super.onSuccess(message);
                    Log.i( "success :" + message);
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                }

                @Override
                public void onFinish() {
                    list.remove(0);
                    if (list.size()>0){
                        Log.i("Finished command : ffmpeg " + Arrays.toString(command));
                        executeFFmpeg(list,type);
                    }else {
                        Log.i("All Finished command : ffmpeg " + Arrays.toString(command));
                        ffmpegListener.onAllFinish(type);
                    }
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }


    private FFmpegListener  ffmpegListener;
    public void  setFFmpegListener(FFmpegListener listener){
        this.ffmpegListener=listener;
    }
    public interface FFmpegListener{
        void onAllFinish(int type);
    }



}