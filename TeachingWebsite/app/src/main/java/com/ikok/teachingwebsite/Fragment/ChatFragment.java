package com.ikok.teachingwebsite.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.fabtransitionactivity.SheetLayout;
import com.ikok.teachingwebsite.Activity.WritePostActivity;
import com.ikok.teachingwebsite.CustomView.ViewPagerIndicatorWhite;
import com.ikok.teachingwebsite.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anonymous on 2016/4/10.
 */
public class ChatFragment extends Fragment implements SheetLayout.OnFabAnimationEndListener {

    private View view = null;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragment = new ArrayList<Fragment>();

    private ViewPagerIndicatorWhite mViewPagerIndicator;
    private List<String> titles = Arrays.asList("文章","活动");
    /**
     * 绿色的浮动按钮
     */
    private FloatingActionButton mFloatingActionButton;

    private SheetLayout mSheetLayout;
    private static final int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_chat,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // ViewPager
        mViewPager = (ViewPager) view.findViewById(R.id.id_chat_viewpager);
        // tab指示器
        mViewPagerIndicator = (ViewPagerIndicatorWhite) view.findViewById(R.id.id_chat_indicator_white);
        // FloatingActionButton
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.id_chat_floatBtn);
        // 点击事件
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), WritePostActivity.class);
//                startActivity(intent);
                mSheetLayout.expandFab();
            }
        });

        /**
         * SheetLayout
         * 设置从边角弹出的颜色
         * 设置绑定的fab
         * 设置动画结束监听
         */
        mSheetLayout = (SheetLayout) view.findViewById(R.id.id_chat_sheet);
        mSheetLayout.setColor(getResources().getColor(R.color.colorMain));
        mSheetLayout.setFab(mFloatingActionButton);
        mSheetLayout.setFabAnimationEndListener(this);


        // 文章列表Fragment
        Fragment chatPostListFragment = new ChatPostListFragment();
        // 活动列表Fragment
        Fragment chatActivityListFragment = new ChatActivityListFragment();

        mFragment.add(chatPostListFragment);
        mFragment.add(chatActivityListFragment);

        mAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        mViewPager.setAdapter(mAdapter);


        // 可见的tab数
        mViewPagerIndicator.setVisibleTabCount(2);
        // tab标题
        mViewPagerIndicator.setTabItemTitles(titles);
        // 为指示器设置ViewPager,并指定默认显示的tab
        mViewPagerIndicator.setViewPager(mViewPager,0);

    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(getContext(), WritePostActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            mSheetLayout.contractFab();
        }
    }


}
