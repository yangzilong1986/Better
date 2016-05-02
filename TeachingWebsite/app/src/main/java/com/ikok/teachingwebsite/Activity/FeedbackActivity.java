package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ikok.teachingwebsite.Entity.Feedback;
import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/21.
 */
public class FeedbackActivity extends Activity {
    /**
     * 顶部条的图片和文字
     */
    private ImageView mTopbarBackBtn;
    private TextView mTopbarTitle;
    /**
     * 反馈的内容
     */
    private MaterialEditText mFeedbackContent;
    private EditText mFeedbackContact;
    private Button mFeedbackBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings_feedback);
//        StatusBarUtil.setColor(FeedbackActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);
        mFeedbackContent = (MaterialEditText) findViewById(R.id.id_settings_feedback_content);
        mFeedbackContact = (EditText) findViewById(R.id.id_settings_feedback_contact);
        mFeedbackBtn = (Button) findViewById(R.id.id_settings_feedback_btn);
        // 设置顶部标题
        mTopbarTitle.setText("意见反馈");
        // 顶部返回按钮
        mTopbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 提交意见反馈按钮
        mFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempContent = mFeedbackContent.getText().toString();
                String tempContact = mFeedbackContact.getText().toString();
                saveFeedbackMsg(tempContent,tempContact);
            }
        });
    }

    /**
     * 保存反馈信息到Bmob云数据库中
     * @param msg 反馈信息
     */
    private void saveFeedbackMsg(String msg, String contact){
        Feedback feedback = new Feedback();
        feedback.setContent(msg);
        feedback.setContact(contact);
        User user = BmobUser.getCurrentUser(this,User.class);
        feedback.setFeedbackUser(user);
        if (!msg.equals("")){
            feedback.save(this, new SaveListener() {

                @Override
                public void onSuccess() {
                    Toast.makeText(FeedbackActivity.this,"反馈信息已提交",Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(int code, String arg0) {
                    Toast.makeText(FeedbackActivity.this,"反馈信息提交失败:"+arg0 + " " + code,Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(FeedbackActivity.this,"反馈内容不能为空",Toast.LENGTH_SHORT).show();
        }

    }

}
