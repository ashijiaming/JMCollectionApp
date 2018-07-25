#include <jni.h>
#include <stdio.h>
#include <unistd.h>
#include <android/log.h>
#include <android/native_window_jni.h>
//封装格式
#include "libavformat/avformat.h"

//缩放
#include "libswscale/swscale.h"

//音频
#include "libswresample/swresample.h"
#include "libyuv.h"

#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"ming",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"ming",FORMAT,##__VA_ARGS__);

#define MAX_AUDIO_FRME_SIZE 48000*4

/**
 * 该文件为最基本的音视频解码与播放，为第一版
 * 未使用线程，直接在主线程中进行解码播放
 */

//简单解码视频文件
JNIEXPORT jstring JNICALL
Java_jm_com_collection_ffmpeg_jni_DecodeVideo_decode(JNIEnv *env, jobject instance, jstring input_,
                                                     jstring output_) {
    const char *input = (*env)->GetStringUTFChars(env, input_, NULL);
    const char *output = (*env)->GetStringUTFChars(env, output_, NULL);
    //1.注册组件
    av_register_all();

    //封装格式上下文
    AVFormatContext *pFormatCtx = avformat_alloc_context();

    //2.打开输入视频文件
    if (avformat_open_input(&pFormatCtx, input, NULL, NULL) != 0) {
        LOGE("%s", "打开输入视频文件失败");
        return (*env)->NewStringUTF(env, "打开输入视频文件失败");
    }
    //3.获取视频信息
    if (avformat_find_stream_info(pFormatCtx, NULL) < 0) {
        LOGE("%s", "获取视频信息失败");
        return (*env)->NewStringUTF(env, "获取视频信息失败");
    }

    //视频解码，需要找到视频对应的AVStream所在pFormatCtx->streams的索引位置
    int video_stream_idx = -1;
    int i = 0;
    for (; i < pFormatCtx->nb_streams; i++) {
        //根据类型判断，是否是视频流
        if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO) {
            video_stream_idx = i;
            break;
        }
    }

    //4.获取视频解码器
    AVCodecContext *pCodeCtx = pFormatCtx->streams[video_stream_idx]->codec;
    AVCodec *pCodec = avcodec_find_decoder(pCodeCtx->codec_id);
    if (pCodec == NULL) {
        LOGE("%s", "无法解码");
        return (*env)->NewStringUTF(env, "无法解码");
    }

    //5.打开解码器
    if (avcodec_open2(pCodeCtx, pCodec, NULL) < 0) {

        return (*env)->NewStringUTF(env, "解码器无法打开");
    }

    //编码数据
    AVPacket *packet = (AVPacket *) av_malloc(sizeof(AVPacket));

    //像素数据（解码数据）
    AVFrame *frame = av_frame_alloc();
    AVFrame *yuvFrame = av_frame_alloc();

    //只有指定了AVFrame的像素格式、画面大小才能真正分配内存
    //缓冲区分配内存
    uint8_t *out_buffer = (uint8_t *) av_malloc(
            avpicture_get_size(AV_PIX_FMT_YUV420P, pCodeCtx->width, pCodeCtx->height));
    //初始化缓冲区
    avpicture_fill((AVPicture *) yuvFrame, out_buffer, AV_PIX_FMT_YUV420P, pCodeCtx->width,
                   pCodeCtx->height);


    //输出文件
    FILE *fp_yuv = fopen(output, "wb");

    //用于像素格式转换或者缩放
    struct SwsContext *sws_ctx = sws_getContext(
            pCodeCtx->width, pCodeCtx->height, pCodeCtx->pix_fmt,
            pCodeCtx->width, pCodeCtx->height, AV_PIX_FMT_YUV420P,
            SWS_BILINEAR, NULL, NULL, NULL);

    int len, got_frame, framecount = 0;

    //通知前台当前解码到第几帧
    jclass cls = (*env)->GetObjectClass(env, instance);
    jmethodID methodId = (*env)->GetMethodID(env, cls, "updateProgress", "(I)I");

    //6.一阵一阵读取压缩的视频数据AVPacket
    while (av_read_frame(pFormatCtx, packet) >= 0) {
        //解码AVPacket->AVFrame
        len = avcodec_decode_video2(pCodeCtx, frame, &got_frame, packet);

        //Zero if no frame could be decompressed
        //非零，正在解码
        if (got_frame) {
            //frame->yuvFrame (YUV420P)
            //转为指定的YUV420P像素帧
            sws_scale(sws_ctx,
                      frame->data, frame->linesize, 0, frame->height,
                      yuvFrame->data, yuvFrame->linesize);

            //向YUV文件保存解码之后的帧数据
            //AVFrame->YUV
            //一个像素包含一个Y
            int y_size = pCodeCtx->width * pCodeCtx->height;
            fwrite(yuvFrame->data[0], 1, y_size, fp_yuv);
            fwrite(yuvFrame->data[1], 1, y_size / 4, fp_yuv);
            fwrite(yuvFrame->data[2], 1, y_size / 4, fp_yuv);

            (*env)->CallIntMethod(env, instance, methodId, framecount);

            LOGI("解码%d帧", framecount++);
        }

        av_free_packet(packet);
    }

    fclose(fp_yuv);

    av_frame_free(&frame);
    avcodec_close(pCodeCtx);
    avformat_free_context(pFormatCtx);


    (*env)->ReleaseStringUTFChars(env, input_, input);
    (*env)->ReleaseStringUTFChars(env, output_, output);

    return (*env)->NewStringUTF(env, "成功");
}


//播放各类格式的视频文件
JNIEXPORT void JNICALL
Java_jm_com_collection_ffmpeg_jni_DecodeVideo_render(JNIEnv *env, jobject instance, jstring input_, jobject surface) {
    const char *input = (*env)->GetStringUTFChars(env, input_, 0);

    //1,注册组件
    av_register_all();

    //封装格式上下文
    AVFormatContext *pFormatCtx = avformat_alloc_context();

    //2.打开输入视频文件
    if (avformat_open_input(&pFormatCtx, input, NULL, NULL) != 0) {
        LOGI("%s", "打开输入文件失败");
        return;
    }

    //3.获取视频信息
    if (avformat_find_stream_info(pFormatCtx, NULL) < 0) {
        LOGI("%s", "获取视频信息失败");
        return;
    }

    //视频解码，需要找到视频对应的AVStream所在pFormatCtx->stream的索引位置
    int video_stream_idx = -1;
    int i = 0;
    for (; i < pFormatCtx->nb_streams; i++) {
        //根据类型判断，是否是视频流
        if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO) {
            video_stream_idx = i;
        }
    }

    //4.获取视频解码器
    AVCodecContext *pCodecCtx = pFormatCtx->streams[video_stream_idx]->codec;
    AVCodec *pCodec = avcodec_find_decoder(pCodecCtx->codec_id);
    if (pCodec == NULL) {
        LOGI("%s", "无法获得解码器");
        return;
    }

    //5.打开解码器
    if (avcodec_open2(pCodecCtx, pCodec, NULL) < 0) {
        LOGI("%s", "打开解码器失败");
        return;
    }

    //一帧编码数据
    AVPacket *packet = (AVPacket *) av_malloc(sizeof(AVPacket));

    //一帧像素数据(将编码数据解码后得到像素数据)
    AVFrame *yuv_frame = av_frame_alloc();
    AVFrame *rgb_frame = av_frame_alloc();

    //进行Native绘制
    //得到窗体
    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env, surface);
    //绘制时的缓冲区
    ANativeWindow_Buffer outBuffer;

    int len, got_frame, frame_count = 0;
    //6.一帧一帧的读取压缩的视频数据AVPacket
    while (av_read_frame(pFormatCtx, packet) >= 0) {
        //解码AVPacket->AVFrame
        len = avcodec_decode_video2(pCodecCtx, yuv_frame, &got_frame, packet);

        //非零，正在解码
        if (got_frame) {
            LOGI("解码%d帧", frame_count++);
            //设置缓冲区的属性（宽，高，像素格式）
            ANativeWindow_setBuffersGeometry(nativeWindow, pCodecCtx->width, pCodecCtx->height,
                                             WINDOW_FORMAT_RGBA_8888);
            ANativeWindow_lock(nativeWindow, &outBuffer, NULL);
            //设置rgb_frame的属性(像素格式，宽高)和缓冲区
            //rgb_frame缓冲区域与outBuffer.bits是同一块内存
            avpicture_fill((AVPicture *) rgb_frame, outBuffer.bits, PIX_FMT_RGBA, pCodecCtx->width,
                           pCodecCtx->height);
            //YUV->RGBA_8888
            I420ToARGB(yuv_frame->data[0], yuv_frame->linesize[0],
                       yuv_frame->data[2], yuv_frame->linesize[2],
                       yuv_frame->data[1], yuv_frame->linesize[1],
                       rgb_frame->data[0], rgb_frame->linesize[0],
                       pCodecCtx->width, pCodecCtx->height);
            ANativeWindow_unlockAndPost(nativeWindow);
            usleep(1000 * 16);

        }
        av_free_packet(packet);
    }
    ANativeWindow_release(nativeWindow);
    av_frame_free(&yuv_frame);
    avcodec_close(pCodecCtx);
    avformat_free_context(pFormatCtx);

    (*env)->ReleaseStringUTFChars(env, input_, input);
}

//解码音频文件，使用Android原生播放
JNIEXPORT void JNICALL
Java_jm_com_collection_ffmpeg_jni_DecodeVideo_sound(JNIEnv *env, jobject instance, jstring input_, jstring output_) {
    const char *input = (*env)->GetStringUTFChars(env, input_, 0);
    const char *output = (*env)->GetStringUTFChars(env, output_, 0);

    //1.注册组件
    av_register_all();

    //2.封装格式上下文
    AVFormatContext *pFormatCtx = avformat_alloc_context();

    //3.打开音频文件
    if (avformat_open_input(&pFormatCtx, input, NULL, NULL) != 0) {
        LOGI("%s", "打开音频文件失败");
        return;
    }
    //4.获取输入文件信息
    if (avformat_find_stream_info(pFormatCtx, NULL) < 0) {
        LOGI("%s", "获取音频文件信息失败");
        return;
    }

    //5.获取音频流索引位置
    int i = 0, audio_stream_idx = -1;
    for (; i < pFormatCtx->nb_streams; i++) {
        if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO) {
            audio_stream_idx = i;
            break;
        }
    }

    //6.获取解码器
    AVCodecContext *codecCtx = pFormatCtx->streams[audio_stream_idx]->codec;
    AVCodec *codec = avcodec_find_decoder(codecCtx->codec_id);
    if (codec == NULL) {
        LOGI("%s", "无法获取音频解码器");
        return;
    }
    //7.打开解码器
    if (avcodec_open2(codecCtx, codec, NULL) < 0) {
        LOGI("%s", "无法打开音频解码器");
        return;
    }

    //压缩数据
    AVPacket *packet = (AVPacket *) av_malloc(sizeof(AVPacket));
    //解压缩数据
    AVFrame *frame = av_frame_alloc();
    //frame->16bit 44100PCM 统一音频采样格式与采样率
    SwrContext *swrCtx = swr_alloc();

    //重采样参数设置---开始
    //输入的采样格式
    enum AVSampleFormat int_sample_fmt = codecCtx->sample_fmt;
    //输出的采样格式16bit PCM
    enum AVSampleFormat out_sample_fmt = AV_SAMPLE_FMT_S16;
    //输入采样率
    int in_sample_rate = codecCtx->sample_rate;
    //输出采样率
    int out_sample_rate = in_sample_rate;
    //获取输入的声道布局
    //根据声道的个数获取默认的声道布局（2个声道。默认立体声stereo）
    uint64_t in_ch_layout = codecCtx->channel_layout;
    //输出声道布局立体声
    uint64_t out_ch_layout = AV_CH_LAYOUT_STEREO;

    swr_alloc_set_opts(swrCtx,
                       out_ch_layout, out_sample_fmt, out_sample_rate,
                       in_ch_layout, int_sample_fmt, in_sample_rate,
                       0, NULL);
    swr_init(swrCtx);

    //输出的声道的个数
    int out_channel_nb = av_get_channel_layout_nb_channels(out_ch_layout);
    //重采样参数设置---------结束

    //JNI 开始调用原生播放音频
    jclass player_class = (*env)->GetObjectClass(env, instance);
    //AudioTrack对象
    jmethodID track_methodId = (*env)->GetMethodID(env, player_class, "createAudioTrack",
                                                   "(II)Landroid/media/AudioTrack;");
    jobject audio_track = (*env)->CallObjectMethod(env, instance, track_methodId, out_sample_rate,
                                                   out_channel_nb);

    //调用AudioTrack.play方法
    jclass audio_class = (*env)->GetObjectClass(env, audio_track);
    jmethodID play_methodId = (*env)->GetMethodID(env, audio_class, "play", "()V");
    (*env)->CallVoidMethod(env, audio_track, play_methodId);

    jmethodID write_methodId = (*env)->GetMethodID(env, audio_class, "write", "([BII)I");
    //JNI 结束

    //16bit 44100 PCM数据
    uint8_t *out_buffer = (uint8_t *) av_malloc(MAX_AUDIO_FRME_SIZE);
    FILE *fp_pcm = fopen(output, "wb");

    int got_frame = 0, index = 0, ret;
    //不断读取压缩数据
    while (av_read_frame(pFormatCtx, packet) >= 0) {
        //解码音频压缩数据
        if (packet->stream_index == audio_stream_idx) {
            //解码
            ret = avcodec_decode_audio4(codecCtx, frame, &got_frame, packet);
            if (ret < 0) {
                LOGI("%s", "音频解码完成");
            }
            //解码一帧成功
            if (got_frame > 0) {
                LOGI("正在解码第%d帧", index++);
                swr_convert(swrCtx, &out_buffer, MAX_AUDIO_FRME_SIZE, frame->data,
                            frame->nb_samples);
                //获取sample的size
                int out_buffer_size = av_samples_get_buffer_size(NULL, out_channel_nb, frame->nb_samples,
                                                                 out_sample_fmt, 1);
                //写入pcm文件
                fwrite(out_buffer, 1, out_buffer_size, fp_pcm);

                //out_buffer缓冲区数据，转换byte数组
                jbyteArray audio_sample_array=(*env)->NewByteArray(env,out_buffer_size);
                jbyte* sample_bytep= (*env)->GetByteArrayElements(env,audio_sample_array,NULL);
                //out_buffer的数据复制到sample_bytep
                memcpy(sample_bytep,out_buffer,out_buffer_size);
                //同步
                (*env)->ReleaseByteArrayElements(env,audio_sample_array,sample_bytep,0);
                //AudioTrack.write PCM数据
                (*env)->CallIntMethod(env,audio_track,write_methodId,
                                          audio_sample_array,0,out_buffer_size);

                //释放局部引用
                (*env)->DeleteLocalRef(env,audio_sample_array);
                usleep(1000*16);
            }
        }
        av_free_packet(packet);
    }

    fclose(fp_pcm);
    av_frame_free(&frame);
    av_free(out_buffer);

    swr_free(&swrCtx);
    avcodec_close(codecCtx);
    avformat_close_input(&pFormatCtx);

    (*env)->ReleaseStringUTFChars(env, input_, input);
    (*env)->ReleaseStringUTFChars(env, output_, output);
}