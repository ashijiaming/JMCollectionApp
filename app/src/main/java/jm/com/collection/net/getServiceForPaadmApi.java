package jm.com.collection.net;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/15.
 * Author Name ShiJiaMing
 * Description : 通过paadm获取相应的数据,Rxjava获取flatMap实践，通过一个接口数据的返回去
 * 获取另外一个数据
 */

public interface getServiceForPaadmApi {

    @POST("SeeResult/SearchPAADMList")
    Observable<ResponseBean> getPaadmList(@Body RequestBody requestBody);

    @POST("SeeResult/GetItemByPAADM")
    Observable<ResponseBean> getTempTold(@Body RequestBody requestBody);


}
