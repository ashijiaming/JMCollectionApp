package jm.com.collection.net;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/8/12.
 * Author Name ShiJiaMing
 * Description :将服务器返回的Response的json格式的数据转化成bean
 */

public class ResponseBean {
    @SerializedName("Response")
    private String Response;

    public String getResponse() {
        return Response;
    }
    public void setResponse(String response) {
        Response = response;
    }
}
