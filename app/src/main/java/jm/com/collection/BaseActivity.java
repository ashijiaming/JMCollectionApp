package jm.com.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import jm.com.collection.utils.AppManagerUtil;


/**
 * Created by Administrator on 2016/10/20.
 * Author Name ShiJiaMing
 * Description : activity基础类
 */
public  abstract class BaseActivity extends AppCompatActivity{

    private static final String TAG="BaseActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManagerUtil.getAppManager().addActivity(this);
        setContentView(getLayoutId());
        initView();
        initData(savedInstanceState);
    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutID
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 该抽象方法就是 初始化view
     */
    protected abstract void initView();
    /**
     * 执行数据的加载
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);

}
