package jm.com.collection.net;

import jm.com.collection.AppConstant;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/8/12.
 * Author Name ShiJiaMing
 * Description ：获取数据列表
 * 这种做法比较的麻烦，因为所有的请求都要添加一个头
 * 所以可以采用自己自己写个拦截器
 * 参考文章http://blog.csdn.net/qqyanjiang/article/details/50948908
 */

public interface GetDepartmentsApi {
    @Headers("Authorization:" + AppConstant.RESTUSRNAME + "/" + AppConstant.RESTUSRPWD)
    @POST("SeeResult/GetLocInfo")
    Call<ResponseBean>getDepartmentList(@Body RequestBody requestBody);
}
