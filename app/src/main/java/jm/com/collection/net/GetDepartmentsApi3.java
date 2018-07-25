package jm.com.collection.net;


import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/12.
 * Author Name ShiJiaMing
 * Description :健康罗湖获取科室列表
 * Retrofit结合Rxjava的请求API 将Call改为rx中的Observable
 */

public interface GetDepartmentsApi3 {
    /**
     * 获取科室列表
     * @param requestBody
     * @return
     */
    @POST("SeeResult/GetLocInfo")
    Observable<ResponseBean> getDepartmentList(@Body RequestBody requestBody);

    /**
     * 获取登录人的信息
     * @param requestBody
     * @return
     */
    @POST("Login/GetAppLoginInfo")
    Observable<ResponseBean> getAppLoginInfo(@Body RequestBody requestBody);

}
