package com.ikok.teachingwebsite.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ikok.teachingwebsite.Entity.Course;
import com.ikok.teachingwebsite.Entity.CourseComment;
import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Anonymous on 2016/5/10.
 */
public class WriteCourseCommentActivity extends BaseActivity {

    /**
     * 顶部条的图片和文字
     */
    private ImageView mTopbarBackBtn;
    private TextView mTopbarTitle;
    private ImageView mTopbarDoneBtn;
    /**
     * 评论内容
     */
    private EditText mCommentContent;
    /**
     * 当前Post对象
     */
    private Course mCourse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post_comment);

        Intent intent = getIntent();
        mCourse = (Course) intent.getSerializableExtra("course");

        // 初始化控件
        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);
        mTopbarDoneBtn = (ImageView) findViewById(R.id.id_top_bar_done);
        mCommentContent = (EditText) findViewById(R.id.id_post_comment_content);
        // 顶部完成按钮显示
        mTopbarDoneBtn.setVisibility(View.VISIBLE);
        // 顶部条标题显示
        mTopbarTitle.setText("写评论");
        // 返回按钮事件
        mTopbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 完成按钮事件,保存评论到该文章下
        mTopbarDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 评论内容
                String content = mCommentContent.getText().toString();
                // 评论用户，获取当前的用户
                final User user = BmobUser.getCurrentUser(WriteCourseCommentActivity.this,User.class);
                // 评论时间
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentTime = format.format(new Date());
//                Log.d("Anonymous","当前时间 is: " + currentTime);
                // 创建数据
                CourseComment comment = new CourseComment();
                comment.setCommentContent(content);
                comment.setCommentUser(user);
                comment.setCommentPublishTime(currentTime);
                comment.setCommentCourse(mCourse);
                comment.save(WriteCourseCommentActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(WriteCourseCommentActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                        WriteCourseCommentActivity.this.finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d("Anonymous","评论失败: " + s + " 错误码：" + i);
                    }
                });

            }
        });

    }
}
