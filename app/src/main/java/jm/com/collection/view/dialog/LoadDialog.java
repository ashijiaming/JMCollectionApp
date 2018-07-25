package jm.com.collection.view.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import jm.com.collection.R;

/**
 * Created by Administrator on 2017/8/18.
 * Author Name ShiJiaMing
 * Description :
 */

public class LoadDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.wave_dailog_loading, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_dialog_loading);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_dialog_message);
        tvMessage.setText("加载中...");
        WaveDrawable mWaveDrawable = new WaveDrawable(getActivity(), R.drawable.chrome_logo);
        imageView.setImageDrawable(mWaveDrawable);
        //自动上涨，水面上涨的效果
        mWaveDrawable.setIndeterminate(true);
        //设置波浪位于图片的高度，波浪的波动大小，波浪的周期长度，以及移动的速度
//        mWaveDrawable.setLevel(5000);
//        mWaveDrawable.setWaveAmplitude(20);
//        mWaveDrawable.setWaveLength(200);
//        mWaveDrawable.setWaveSpeed(10);
        return view;
    }
}
