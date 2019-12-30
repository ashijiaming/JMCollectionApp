package com.jm.media.command;

import android.content.Context;

import com.jm.media.command.exceptions.FFmpegCommandAlreadyRunningException;
import com.jm.media.util.LogInfo;

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
                        LogInfo.i("Load Failure");
                    }

                    @Override
                    public void onSuccess() {
                        LogInfo.i("Load Success");
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
                    LogInfo.i("onStart : ffmpeg" + command);
                }

                public void onProgress(String s) {
                    LogInfo.i("progress :" + s);
                }

                @Override
                public void onSuccess(String message) {
                    super.onSuccess(message);
                    LogInfo.i( "success :" + message);
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                }

                @Override
                public void onFinish() {
                    list.remove(0);
                    if (list.size()>0){
                        LogInfo.i("Finished command : ffmpeg " + Arrays.toString(command));
                        executeFFmpeg(list,type);
                    }else {
                        LogInfo.i("All Finished command : ffmpeg " + Arrays.toString(command));
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
