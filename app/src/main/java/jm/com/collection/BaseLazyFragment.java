package jm.com.collection;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/10/20.
 * Author Name ShiJiaMing
 * Description : fragment 基础类
 */

public abstract class BaseLazyFragment extends Fragment {

    private static final  String TAG="BaseLazyFragment";
    private Context mContext;
    protected View mView;
    protected boolean isFragmentShow = false;
    /** onCreateView(这里即doBusiness) 方法是否执行也就是initView执行完后 */
    protected boolean isBusinessDo = false;
    /**
     * 获得全局的，防止使用getActivity()为空
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {
        if (mView == null) {
            mContext = getContext();
            mView = View.inflate(mContext, getLayoutId(), null);
            initView(mView,savedInstanceState);
            isBusinessDo=true;
            if (isBusinessDo&&isFragmentShow) {
                initDataLazy();
            }
        } else {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，
            // 要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        return mView;
    }

    /**
     * 该方法告诉用户是否可见，通过该方法实现Fragment的懒加载，即Fragment可见的时候加载数据
     * 懒加载对于viewpager+Fragment加载数据很有用处,这样就可以不必再activity中将所有数据都加载完
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isFragmentShow=isVisibleToUser;
            if (isBusinessDo&&isFragmentShow) {
                initDataLazy();
            }
        }else {
            isFragmentShow=false;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutID
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 该抽象方法就是 初始化view
     * @param view
     * @param savedInstanceState
     */
    protected abstract void initView(View view, Bundle savedInstanceState);
    /**
     * 执行数据的加载
     */
    protected abstract void initDataLazy();
}