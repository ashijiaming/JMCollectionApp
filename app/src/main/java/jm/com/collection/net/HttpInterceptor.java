package jm.com.collection.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/12.
 * Author Name ShiJiaMing
 * Description :自己定义拦截器添加头
 * @Headers("Authorization:" + LdAppConstant.RESTUSRNAME + "/" +LdAppConstant.RESTUSRPWD)
 */

public class HttpInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder=chain.request().newBuilder();
        String params=LdAppConstant.RESTUSRNAME + "/" +LdAppConstant.RESTUSRPWD;
        Request request = builder.addHeader("Authorization", params)
                .build();
        return chain.proceed(request);
    }
}