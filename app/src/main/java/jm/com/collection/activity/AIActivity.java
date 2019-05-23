package jm.com.collection.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jm.com.collection.AppConstant;
import jm.com.collection.R;
import jm.com.collection.camera.CameraSurfaceHolder;
import jm.com.collection.entity.EventLocal;
import jm.com.collection.net.HttpClient;
import jm.com.collection.net.ResponseString;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public class AIActivity extends AppCompatActivity {

    private static final String TAG = "AIActivity";
    SurfaceView surfaceView;
    @Bind(R.id.tv_result)
    TextView tv_result;

    CameraSurfaceHolder mCameraSurfaceHolder = new CameraSurfaceHolder();
    private Retrofit retrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        surfaceView = findViewById(R.id.surfaceView);
        mCameraSurfaceHolder.setCameraSurfaceHolder(AIActivity.this, surfaceView);
        if (retrofit == null) {
            retrofit = HttpClient.getInstance();
        }
    }

    @OnClick({R.id.btn_image_capture,R.id.btn_image_add, R.id.btn_image_search})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_image_capture:
                mCameraSurfaceHolder.takePhoto();
                break;
            case R.id.btn_image_add:
                addImage();
                break;
            case R.id.btn_image_search:
                 searchImage();
                break;
        }
    }

    @Subscribe
    public void onMessageEvent(final EventLocal eventLocal){
        tv_result.post(new Runnable() {
            @Override
            public void run() {
                tv_result.setText(eventLocal.getValue());
            }
        });
        Log.i(TAG,eventLocal.getValue());
    }


    public void addImage() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "jm");
            jsonObject.put("img", "123");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        File file = new File(AppConstant.imagePath);
        if (!file.exists()) {
            Log.i(TAG, "文件不存在");
            return;
        }
        tv_result.setText("入库中...");
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用file
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        ImageApi imageApi = retrofit.create(ImageApi.class);
        imageApi.addImage(body).enqueue(new Callback<ResponseString>() {
            @Override
            public void onResponse(Call<ResponseString> call, Response<ResponseString> response) {
                ResponseString responseResult = response.body();
                if (responseResult==null){
                    tv_result.setText("入库失败,结果为空");
                    return;
                }
                Log.i(TAG, "入库请求成功" + responseResult.getMessage());
                String result = responseResult.getResult();
                tv_result.setText("入库结果："+result);
            }

            @Override
            public void onFailure(Call<ResponseString> call, Throwable t) {
                Log.i(TAG, "入库请求失败" + t.toString());
                tv_result.setText("入库失败，请重试");
            }
        });
    }


    public void searchImage() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "jm");
            jsonObject.put("img", "123");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        File file = new File(AppConstant.imagePath);
        if (!file.exists()) {
            Log.i(TAG, "文件不存在");
            return;
        }
        tv_result.setText("识别中...");
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用file
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        ImageApi imageApi = retrofit.create(ImageApi.class);
        imageApi.searchImage(body).enqueue(new Callback<ResponseString>() {
            @Override
            public void onResponse(Call<ResponseString> call, Response<ResponseString> response) {
                ResponseString responseResult = response.body();
                if (responseResult==null){
                    tv_result.setText("识别失败,结果为空");
                    return;
                }
                Log.i(TAG, "识别请求成功" + responseResult.getMessage());
                String result = responseResult.getResult();
                tv_result.setText("识别结果："+result);
            }

            @Override
            public void onFailure(Call<ResponseString> call, Throwable t) {
                Log.i(TAG, "识别请求失败" + t.toString());
                tv_result.setText("识别失败，请重试");
            }
        });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public interface ImageApi {
        @Multipart
        @POST("add")
        Call<ResponseString> addImage(@Part MultipartBody.Part file);

        @Multipart
        @POST("search")
        Call<ResponseString> searchImage(@Part MultipartBody.Part file);
    }
}
