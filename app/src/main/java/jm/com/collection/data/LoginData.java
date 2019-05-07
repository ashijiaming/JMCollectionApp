package jm.com.collection.data;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * 功能描述：
 *
 * @author ShiJiaMing
 * @create： 2019/3/23
 */
public class LoginData {

    Activity mContext;

    public LoginData(Activity mContext) {
        this.mContext = mContext;
    }

    /**
     * 暴露给JS的方法
     */
    @JavascriptInterface
    public void setData(String account, String password) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("账户：").append(account)
                .append(" 密码：").append(password);
        Toast.makeText(mContext,stringBuffer.toString(),Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void onBack(){
        mContext.finish();
    }

}
