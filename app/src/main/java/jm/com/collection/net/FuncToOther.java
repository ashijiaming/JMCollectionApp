package jm.com.collection.net;

import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import jm.com.collection.utils.EncryptUtil;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/8/14.
 * Author Name ShiJiaMing
 * Description :通过map进行变换,将加密的结果转换成List<Map>
 * 返回结果的字段是Result，Result里面有一个List集合
 */

public class FuncToOther implements Func1<ResponseBean,List<Map>> {

    private static final String TAG ="FuncToTable";
    private static FuncToOther resultToList=new FuncToOther();
    private FuncToOther(){}

    public static FuncToOther getInstance(){
        return resultToList;
    }
    @Override
    public List<Map> call(ResponseBean responseBean) {
        String string = responseBean.getResponse().toString();
        String decrypt = EncryptUtil.Decrypt(string, LdAppConstant.ENCRYPTKEY);
        List<Map> mapList = null;
        try {
            JSONObject jsonObject = new JSONObject(decrypt);
            boolean success = jsonObject.getBoolean("success");
            if (success){
                Log.i(TAG,"当前线程名称："+Thread.currentThread().getName());
                JSONObject result = jsonObject.getJSONObject("Result");
                String paadms = result.getString("PAADMS");
                mapList = GsonUtil.getListJson(paadms,Map.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mapList;
    }
}
