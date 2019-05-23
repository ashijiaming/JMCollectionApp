package jm.com.collection.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import jm.com.collection.AppConstant;
import jm.com.collection.entity.EventLocal;

/**
 * Created by zhousong on 2016/9/28.
 * 单独的任务类。继承AsyncTask，来处理从相机实时获取的耗时操作
 */
public class FaceTask extends AsyncTask {
    private byte[] mData;
    Camera mCamera;
    private static final String TAG = "CameraTag";
    private boolean isTake = false;

    //构造函数
    FaceTask(byte[] data, Camera camera) {
        this.mData = data;
        this.mCamera = camera;

    }

    @Override
    protected Object doInBackground(Object[] params) {
        Camera.Parameters parameters = mCamera.getParameters();
        int imageFormat = parameters.getPreviewFormat();//设置该参数横屏
        int w = parameters.getPreviewSize().width;
        int h = parameters.getPreviewSize().height;

        Rect rect = new Rect(0, 0, w, h);
        YuvImage yuvImg = new YuvImage(mData, ImageFormat.NV21, w, h, null);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            yuvImg.compressToJpeg(rect, 100, outputStream);

            Bitmap bitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size());
            Log.i(TAG, "onPreviewFrame: bitmap:" + bitmap.toString());
            if (isTake) {
                isTake = false;
                //若要存储可以用下列代码，格式为jpg
                // 旋转
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);
                try {
                    FileOutputStream fos=new FileOutputStream(AppConstant.imagePath);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    Log.i(TAG,"保存图片成功");
                    EventBus.getDefault().post(new EventLocal("1","图片捕获成功"));
                }catch (Exception e){
                    e.printStackTrace();
                }

                 //这种方式图片是横屏的
//                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/fp.jpg"));
//                yuvImg.compressToJpeg(rect, 100, bos);
//                bos.flush();
//                bos.close();
                mCamera.startPreview();

            }

        } catch (Exception e) {
            Log.e(TAG, "onPreviewFrame: 获取相机实时数据失败" + e.getLocalizedMessage());
        }
        return null;
    }

    public void takePhoto() {
        isTake = true;
    }
}
