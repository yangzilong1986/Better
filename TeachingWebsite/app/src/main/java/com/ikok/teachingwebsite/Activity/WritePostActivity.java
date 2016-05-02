package com.ikok.teachingwebsite.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ikok.teachingwebsite.Entity.Post;
import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/25.
 */
public class WritePostActivity extends BaseActivity {

    /**
     * 顶部条的图片和文字
     */
    private ImageView mTopbarBackBtn;
    private TextView mTopbarTitle;
    private ImageView mTopbarDoneBtn;
    /**
     * 文章内容
     */
    private EditText mPostTitle;
    private EditText mPostContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enterFromBottomAnimation();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_write_post);
//        StatusBarUtil.setColor(WritePostActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
        // 初始化控件
        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);
        mTopbarDoneBtn = (ImageView) findViewById(R.id.id_top_bar_done);
        mPostTitle = (EditText) findViewById(R.id.id_write_post_title);
        mPostContent = (EditText) findViewById(R.id.id_write_post_content);
        // 顶部完成按钮显示
        mTopbarDoneBtn.setVisibility(View.VISIBLE);
        // 顶部条标题显示
        mTopbarTitle.setText("写文章");
        // 返回按钮事件
        mTopbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // 完成按钮事件
        mTopbarDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 文章标题
                String title = mPostTitle.getText().toString();
                String content = mPostContent.getText().toString();
                // 评论用户，获取当前的用户
                final User user = BmobUser.getCurrentUser(WritePostActivity.this,User.class);
                // 评论时间
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentTime = format.format(new Date());
//                Log.d("Anonymous","当前时间 is: " + currentTime);

                Post post = new Post();
                post.setPostTitle(title);
                post.setPostContent(content);
                post.setPostPublishTime(currentTime);
                post.setPostAuthor(user);
                post.save(WritePostActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(WritePostActivity.this,"发表文章成功",Toast.LENGTH_SHORT).show();
                        WritePostActivity.this.finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d("Anonymous","评论失败: " + s + " 错误码：" + i);
                    }
                });

            }
        });

    }

    @Override
    protected void onPause() {
        exitToBottomAnimation();
        super.onPause();
    }
}
