package jm.com.collection.net;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by ShiJiaMing on 2018/4/12.
 * Description
 */

public class ResponseString {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private String result;


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

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
}
