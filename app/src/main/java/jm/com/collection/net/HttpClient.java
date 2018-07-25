package jm.com.collection.net;



import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import jm.com.collection.utils.EncryptUtil;
import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/8/12.
 * Author Name ShiJiaMing
 * Description : Retrofit简单的封装
 */

public class HttpClient {
    private static Retrofit retrofit;
    private static final int DEFAULT_TIMEOUT=5;
    private HttpClient(){}

    public static Retrofit getInstance(){
        if (retrofit==null){
            OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                    .authenticator(Authenticator.NONE)
                    .addInterceptor(new HttpInterceptor())
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

            retrofit=new Retrofit.Builder()
                    .client(okHttpClient.build())
                    .baseUrl(LdAppConstant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static RequestBody getRequestBody(Map object){
        String encrypt = EncryptUtil.Encrypt(GsonUtil.parseJson(object), LdAppConstant.ENCRYPTKEY);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request",encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
       return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
    }
}
