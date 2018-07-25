package jm.com.collection.design.parallel;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import jm.com.collection.R;

/**
 * Created by ShiJiaMing on 2018/4/16.
 * Description
 */

public class WelcomePagerTransformer implements ViewPager.PageTransformer,ViewPager.OnPageChangeListener{

    private static final float ROT_MOD=-15f;
    private int pageIndex;
    private boolean pageChanged=true;

    /**
     * 此方法是滑动的时候每一个页面View都会调用的该方法
     * page：当前页面
     * position ：当前滑动的位置
     * 视差效果：在View正常滑动的情况下，给当前View或者当前View里面的每一个子View来一个加速度
     * 而且每一个加速度大小不一样
     */
    @Override
    public void transformPage(View page, float position) {
        ViewGroup viewGroup = page.findViewById(R.id.rl_percent_layout);
        final MyScrollView myScrollView = viewGroup.findViewById(R.id.scroll_my);
        View imageView2_2 = viewGroup.findViewById(R.id.iv_pager2_2);
        View imageView2_1 = viewGroup.findViewById(R.id.iv_pager2_1);
        View bg_container = viewGroup.findViewById(R.id.bg_container);

        int bg1_green = page.getContext().getResources().getColor(R.color.bg1_green);
        int bg2_blue = page.getContext().getResources().getColor(R.color.bg2_blue);

        Integer tag =(Integer)page.getTag();
        View parent = (View) page.getParent();

        ArgbEvaluator evaluator=new ArgbEvaluator();
        int color=bg1_green;
        if (tag.intValue()==pageIndex){
            switch (pageIndex){
                case 0:
                    color =(int)evaluator.evaluate(Math.abs(position), bg1_green, bg2_blue);
                    break;
                case 1:
                    color =(int)evaluator.evaluate(Math.abs(position), bg2_blue, bg1_green);
                    break;
                case 2:
                    color =(int)evaluator.evaluate(Math.abs(position), bg1_green, bg2_blue);
                    break;
            }
            parent.setBackgroundColor(color);
        }

        if (position==0){
            //pageChanged作用--解决问题：只有在切换界面的时候才展示平移动画，
            //如果不判断则会只是移动一点点当前页面松开也会执行一次平移动画。
            if (pageChanged){
                imageView2_1.setVisibility(View.VISIBLE);
                imageView2_2.setVisibility(View.VISIBLE);

                ObjectAnimator animator_image1=ObjectAnimator.ofFloat(imageView2_1,"translationX",0,-imageView2_1.getWidth());
                animator_image1.setDuration(400);
                animator_image1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        myScrollView.smoothScrollTo((int) (myScrollView.getWidth()*animation.getAnimatedFraction()),0);
                    }
                });
                animator_image1.start();

                ObjectAnimator animator_image2=ObjectAnimator.ofFloat(imageView2_2,"translationX",imageView2_2.getWidth(),0);
                animator_image2.setDuration(400);
                animator_image2.start();
                pageChanged=false;
            }
        }else if (position==-1||position==1){//所有效果复原
            imageView2_2.setTranslationX(0);
            imageView2_1.setTranslationY(0);
            myScrollView.smoothScrollTo(0,0);
        }else if (position<1&&position>-1){
            int width = imageView2_1.getWidth();
            int height = imageView2_1.getHeight();
            float rotation = ROT_MOD * position * -1.25f;

           //这里不去分别处理imageView2_1、imageView2_2，而是用包裹的父容器执行动画，目的是避免难以处理两个bg的属性位置恢复。
            bg_container.setPivotX(width*0.5f);
            bg_container.setPivotY(height);
            bg_container.setRotation(rotation);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        pageIndex=position;
        pageChanged=true;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
