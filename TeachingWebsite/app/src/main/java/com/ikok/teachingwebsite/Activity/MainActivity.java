package com.ikok.teachingwebsite.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ikok.teachingwebsite.Fragment.ChatFragment;
import com.ikok.teachingwebsite.Fragment.CoursesFragment;
import com.ikok.teachingwebsite.Fragment.MyselfFragment;
import com.ikok.teachingwebsite.R;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/10.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    /**
     * 四个tab选项卡
     */
    private LinearLayout mCourseTab;
    private LinearLayout mChatTab;
    private LinearLayout mMyselfTab;
    /**
     * 四个tab选项卡中的图片
     */
    private ImageView mCourseImg;
    private ImageView mChatImg;
    private ImageView mMyselfImg;
    /**
     * 四个tab选项卡中的文字
     */
    private TextView mCourseText;
    private TextView mChatText;
    private TextView mMyselfText;
    /**
     * 四个tab选项卡对应的视图
     */
    private Fragment mCourse;
    private Fragment mChat;
    private Fragment mMyself;
    /**
     * tab文字默认与选中的颜色
     */
    private static final int TEXT_DEFAULT_COLOR = 0xFF929292;
    private static final int TEXT_SELECT_COLOR = 0xFF48C56A;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
//        StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.white));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
        // 初始化控件
        initViews();
        // 绑定点击监听器
        initEvents();
        // 默认第一个可见
        setSelectItem(0);

    }


    private void initEvents() {
        mCourseTab.setOnClickListener(this);
        mChatTab.setOnClickListener(this);
        mMyselfTab.setOnClickListener(this);
    }

    private void initViews() {

        mCourseTab = (LinearLayout) findViewById(R.id.id_tab_course);
        mChatTab = (LinearLayout) findViewById(R.id.id_tab_chat);
        mMyselfTab = (LinearLayout) findViewById(R.id.id_tab_myself);

        mCourseImg = (ImageView) findViewById(R.id.id_tab_img_course);
        mChatImg = (ImageView) findViewById(R.id.id_tab_img_chat);
        mMyselfImg = (ImageView) findViewById(R.id.id_tab_img_myself);

        mCourseText = (TextView) findViewById(R.id.id_tab_text_course);
        mChatText = (TextView) findViewById(R.id.id_tab_text_chat);
        mMyselfText = (TextView) findViewById(R.id.id_tab_text_myself);
    }

    @Override
    public void onClick(View v) {
        // 把所有图片变成灰色,文字变成灰色
        resetImgsAndTextColor();
        switch (v.getId()){
            case R.id.id_tab_course:
                setSelectItem(0);
//                StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.white));
                StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
                break;
            case R.id.id_tab_chat:
                setSelectItem(1);
//                StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.colorMain));
                StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
                break;
            case R.id.id_tab_myself:
                setSelectItem(2);
//                StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.colorSettings));
                StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorSettings));

                break;
        }

    }

    /**
     * 将图片全部置为灰色，文字颜色全部置为灰色
     */
    private void resetImgsAndTextColor() {
        mCourseImg.setImageResource(R.drawable.icon_course_24dp);
        mChatImg.setImageResource(R.drawable.icon_chat_24dp);
        mMyselfImg.setImageResource(R.drawable.icon_user_24dp);

        mCourseText.setTextColor(TEXT_DEFAULT_COLOR);
        mChatText.setTextColor(TEXT_DEFAULT_COLOR);
        mMyselfText.setTextColor(TEXT_DEFAULT_COLOR);
    }

    /**
     * 设置选中的tab的图片变亮、文字变色，以及相应的视图显示
     */
    private void setSelectItem(int i){
        // 取得 FragmentManager，以便开启事务
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        // 先隐藏Fragment
        hideFragment(transaction);

        switch (i){
            case 0:
                // 当该视图不存在时，创建该视图，并显示该视图
                if (mCourse == null){
                    mCourse = new CoursesFragment();
                    // 显示该视图
                    transaction.add(R.id.id_main_content,mCourse);
                } else {
                    // 当视图存在时，直接显示该视图
                    transaction.show(mCourse);
                }
                mCourseImg.setImageResource(R.drawable.icon_course_selected_24dp);
                // 更改文字颜色
                mCourseText.setTextColor(TEXT_SELECT_COLOR);
                break;
            case 1:
                if (mChat == null){
                    mChat = new ChatFragment();
                    transaction.add(R.id.id_main_content,mChat);
                } else {
                    transaction.show(mChat);
                }
                mChatImg.setImageResource(R.drawable.icon_chat_selected_24dp);
                mChatText.setTextColor(TEXT_SELECT_COLOR);
                break;
            case 2:
                if (mMyself == null){
                    mMyself = new MyselfFragment();
                    transaction.add(R.id.id_main_content,mMyself);
                } else {
                    transaction.show(mMyself);
                }
                mMyselfImg.setImageResource(R.drawable.icon_user_selected_24dp);
                mMyselfText.setTextColor(TEXT_SELECT_COLOR);
                break;
        }
        // 提交事务
        transaction.commit();
    }

    /**
     * 隐藏Fragment
     * @param transaction 事务
     */
    private void hideFragment(FragmentTransaction transaction) {
        // 当视图不为空时，隐藏该视图
        if (mCourse != null){
            transaction.hide(mCourse);
        }
        if (mChat != null){
            transaction.hide(mChat);
        }
        if (mMyself != null){
            transaction.hide(mMyself);
        }
    }

}
