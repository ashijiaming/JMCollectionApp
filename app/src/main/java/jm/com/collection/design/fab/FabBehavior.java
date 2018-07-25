package jm.com.collection.design.fab;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.jar.Attributes;

/**
 * Created by ShiJiaMing on 2018/4/17.
 * Description
 */

public class FabBehavior extends FloatingActionButton.Behavior {
    private boolean visible=true;//是否可见

    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        // 当观察的View（RecyclerView）发生滑动的开始的时候回调的
        // axes:滑动关联轴， 我们现在只关心垂直的滑动。
        return axes== ViewCompat.SCROLL_AXIS_VERTICAL|| super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed>0&&visible){
            visible=true;
            onHide(child);
        }else if (dyConsumed<0){
            visible=false;
            onShow(child);
        }
    }

    private void onShow(FloatingActionButton child) {
        // toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(3));
        ViewGroup.MarginLayoutParams layoutParams= (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        // fab.animate().translationY(fab.getHeight()+layoutParams.bottomMargin).setInterpolator(new AccelerateInterpolator(3))
        ViewCompat.animate(child).scaleX(0f).scaleY(0f).start();
    }

    private void onHide(FloatingActionButton child) {
        // 显示动画--属性动画
        // toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3));

        ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
        // fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3));
        ViewCompat.animate(child).scaleX(0f).scaleY(0f).start();
    }
}
