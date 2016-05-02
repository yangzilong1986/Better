package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ikok.teachingwebsite.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/21.
 */
public class ChangePasswordActivity extends Activity {

    /**
     * 顶部条的图片和文字
     */
    private ImageView mTopbarBackBtn;
    private TextView mTopbarTitle;
    private ImageView mTopbarDoneBtn;
    /**
     * 密码文本
     */
    private MaterialEditText mOldPasswordText;
    private MaterialEditText mNewPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings_change_password);
//        StatusBarUtil.setColor(ChangePasswordActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
//        Log.d("Anonymous","password is: " + password);
        // 初始化控件
        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);
        mTopbarDoneBtn = (ImageView) findViewById(R.id.id_top_bar_done);
        // 两次密码
        mOldPasswordText = (MaterialEditText) findViewById(R.id.id_settings_old_password_text);
        mNewPasswordText = (MaterialEditText) findViewById(R.id.id_settings_new_password_text);
        // 将完成按钮显示
        mTopbarDoneBtn.setVisibility(View.VISIBLE);
        // 顶部条标题显示
        mTopbarTitle.setText("修改签名");

        // 返回按钮事件
        mTopbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 完成按钮事件
        mTopbarDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = mOldPasswordText.getText().toString();
                String newPassword = mNewPasswordText.getText().toString();
                // 昵称不能为空字符串
                if (!newPassword.trim().equals("") && newPassword.length() <= 16){
                    BmobUser.updateCurrentUserPassword(ChangePasswordActivity.this, oldPassword, newPassword, new UpdateListener() {

                        @Override
                        public void onSuccess() {
                            Toast.makeText(ChangePasswordActivity.this,"密码修改成功，可以用新密码进行登录啦",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            // 根据错误代码提示不同信息
                            if (code == 210){
                                Toast.makeText(ChangePasswordActivity.this,"密码修改失败,旧密码不正确",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this,"密码修改失败:"+msg+ " " +code,Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } else {
                    Toast.makeText(ChangePasswordActivity.this,"输入有误，请重试",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
