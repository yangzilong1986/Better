package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ikok.teachingwebsite.Entity.Comment;
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
 * Created by Anonymous on 2016/4/24.
 */
public class WritePostCommentActivity extends Activity {

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
    private Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_write_post_comment);
//        StatusBarUtil.setColor(WritePostCommentActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
        Intent intent = getIntent();
        mPost = (Post) intent.getSerializableExtra("post");

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
                final User user = BmobUser.getCurrentUser(WritePostCommentActivity.this,User.class);
                // 评论时间
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentTime = format.format(new Date());
//                Log.d("Anonymous","当前时间 is: " + currentTime);
                // 创建数据
                Comment comment = new Comment();
                comment.setCommentContent(content);
                comment.setCommentUser(user);
                comment.setCommentPublishTime(currentTime);
                comment.setCommentPost(mPost);
                comment.save(WritePostCommentActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(WritePostCommentActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                        WritePostCommentActivity.this.finish();
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
