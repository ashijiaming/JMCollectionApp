package jm.com.collection.net;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by ShiJiaMing on 2018/4/12.
 * Description
 */

public class ResponseMap {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private Map result;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Map getResult() {
        return result;
    }
    public void setResult(Map result) {
        this.result = result;
    }
}
