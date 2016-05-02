package com.ikok.teachingwebsite.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ikok.teachingwebsite.Activity.MyselfLearnedCourseActivity;
import com.ikok.teachingwebsite.Activity.MyselfLikedPostActivity;
import com.ikok.teachingwebsite.Activity.MyselfSettingsActivity;
import com.ikok.teachingwebsite.Activity.MyselfWritedPostActivity;
import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.R;
import com.ikok.teachingwebsite.Util.UserImgLoader;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anonymous on 2016/4/10.
 */
public class MyselfFragment extends Fragment {

    private View view = null;
    /**
     * 个人界面底部三个tab
     */
    private LinearLayout mLearnedCourse;
    private LinearLayout mLikedPost;
    private LinearLayout mWritedPosts;
    /**
     * 个人界面头像部分
     */
    private CircleImageView mUserImg;
    private TextView mUserName;
    private TextView mUserIntro;
    private ImageView mUserSettings;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_myself,container,false);

        mUserImg = (CircleImageView) view.findViewById(R.id.id_myself_img);
        mUserName = (TextView) view.findViewById(R.id.id_myself_username);
        mUserIntro = (TextView) view.findViewById(R.id.id_myself_intro);
        mUserSettings = (ImageView) view.findViewById(R.id.id_myself_settings);

        getCurrentUser();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLearnedCourse = (LinearLayout) view.findViewById(R.id.id_myself_layout_learned);
        mLikedPost = (LinearLayout) view.findViewById(R.id.id_myself_layout_liked);
        mWritedPosts = (LinearLayout) view.findViewById(R.id.id_myself_layout_write);

        // 修改个人设置的按钮
        mUserSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyselfSettingsActivity.class);
                startActivity(intent);
            }
        });

        // 个人学习课程的按钮
        mLearnedCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyselfLearnedCourseActivity.class);
                startActivity(intent);
            }
        });

        // 个人收藏文章的按钮
        mLikedPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyselfLikedPostActivity.class);
                startActivity(intent);
            }
        });

        // 个人写的文章的按钮
        mWritedPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyselfWritedPostActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 碎片恢复时，重新加载一次页面，为了刷新更改的昵称和签名
     */
    @Override
    public void onResume() {
        super.onResume();
        getCurrentUser();
    }

    /**
     *  得到当前用户，并显示相关信息
     */
    private void getCurrentUser() {
        User user = BmobUser.getCurrentUser(getContext(),User.class);
//        Log.d("Anonymous"," is: " + user + "/" + user.getUsername());
        if (user != null) {
            // 允许用户使用应用
            // 用户名，没有昵称时使用用户名，有昵称时使用昵称
            if (user.getNickName() == null){
                mUserName.setText(user.getUsername());
            } else {
                mUserName.setText(user.getNickName());
            }

            // 签名
            if (user.getUserIntro() == null){
                mUserIntro.setText("写点儿什么吧..");
            } else {
                mUserIntro.setText(user.getUserIntro());
            }
            // 头像
            if (user.getProfileImg() == null){

            } else {
                // 当用户自定义了头像后，加载用户设置的头像
                String fileUrl = user.getProfileImg().getFileUrl(getContext());
                new UserImgLoader(mUserImg).execute(fileUrl);
            }

        } else {
            // 缓存对象为空时，可打开用户注册界面
        }
    }


}
