package com.ikok.teachingwebsite.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;

import com.ikok.teachingwebsite.Fragment.GuideOne;
import com.ikok.teachingwebsite.Fragment.GuideThree;
import com.ikok.teachingwebsite.Fragment.GuideTwo;
import com.ikok.teachingwebsite.R;
import com.ikok.teachingwebsite.Util.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anonymous on 2016/3/26.
 */
public class GuideActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragment = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        mViewPager = (ViewPager) findViewById(R.id.id_guide_viewpager);

        Fragment guide1 = new GuideOne();
        Fragment guide2 = new GuideTwo();
        Fragment guide3 = new GuideThree();

        mFragment.add(guide1);
        mFragment.add(guide2);
        mFragment.add(guide3);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Log.d("Anonymous"," is: " + position);
                return mFragment.get(position);
            }

            @Override
            public int getCount() {

                return mFragment.size();
            }
        };

        // 为ViewPager添加动画效果,3.0以上可用
        mViewPager.setPageTransformer(true,new DepthPageTransformer());
//        mViewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        mViewPager.setAdapter(mAdapter);

    }
}
