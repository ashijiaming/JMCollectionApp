package jm.com.collection.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import jm.com.collection.R;
import jm.com.collection.design.parallel.TranslateFragment;
import jm.com.collection.design.parallel.WelcomePagerTransformer;

/**
 * Created by ShiJiaMing on 2018/4/16.
 * Description
 */

public class ParallelActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private int[] layouts={
            R.layout.pager_welcome1,
            R.layout.pager_welcome2,
            R.layout.pager_welcome3
    };
    private WelcomePagerTransformer transformer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallel);
        viewPager=findViewById(R.id.view_pager);
        WelcomePagerAdapter pagerAdapter = new WelcomePagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);

        transformer=new WelcomePagerTransformer();
        viewPager.setPageTransformer(true,transformer);
        viewPager.addOnPageChangeListener(transformer);
    }

    class WelcomePagerAdapter extends FragmentPagerAdapter{

        public WelcomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TranslateFragment fragment = new TranslateFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("layoutId",layouts[position]);
            bundle.putInt("pageIndex",position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
