package jm.com.collection;

import android.os.Environment;

/**
 * Created by Administrator on 2017/8/12.
 * Author Name ShiJiaMing
 * Description : ld全局类
 */

public class AppConstant {
    // 请求WCF Restful帐号
    public static final String RESTUSRNAME = "V0H66DH26V2H68LB";
    // 请求WCF Restful密码
    public static final String RESTUSRPWD = "0RF04XZPLDL6620V";
    // 数据加密Key值
    public static final  String ENCRYPTKEY = "4NRB426266F6V6B2";

    public static final String BASE_URL="http://192.168.1.40:8080/";

    public static final  String imagePath= Environment.getExternalStorageDirectory().getPath() + "/fp.jpg";
}
