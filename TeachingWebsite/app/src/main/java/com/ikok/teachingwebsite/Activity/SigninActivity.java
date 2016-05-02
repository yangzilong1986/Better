package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.R;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/10.
 */
public class SigninActivity extends Activity {

    private EditText mInputUsername;
    private EditText mInputPassword;
    private EditText mConfirmPassword;
    private Button mSigninBtn;
    private TextView mAccountInfo;

    private boolean isAccountAvailable = false;
    /**
     * 顶部条的图片和文字
     */
    private ImageView mTopbarBackBtn;
    private TextView mTopbarTitle;
    private TextView mTopbarSign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signin);
//        StatusBarUtil.setColor(SigninActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
        /**
         * 初始化 Bmob SDK
         */
        Bmob.initialize(this, "6672f54e9508f8fad63daa61f2b59c9c");
        /**
         * 初始化控件
         */
        mInputUsername = (EditText) findViewById(R.id.id_sign_in_username_text);
        mInputPassword = (EditText) findViewById(R.id.id_sign_in_password_text);
        mConfirmPassword = (EditText) findViewById(R.id.id_sign_in_confirm_text);
        mSigninBtn = (Button) findViewById(R.id.id_sign_in_btn);
        mAccountInfo = (TextView) findViewById(R.id.id_sign_in_account_info);

        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);
        mTopbarSign = (TextView) findViewById(R.id.id_top_bar_sign);
        // 顶部条标题显示
        mTopbarTitle.setText("注册");
        // 返回按钮事件
        mTopbarBackBtn.setVisibility(View.GONE);
        // 顶部登录注册按钮
        mTopbarSign.setVisibility(View.VISIBLE);
        mTopbarSign.setText("登录");
        mTopbarSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


        /**
         * 用户名输入框失去焦点时，判断用户名是否可用
         */
        mInputUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String username = mInputUsername.getText().toString();
                    new LoadUserTask().execute(username);
                }
            }
        });

        /**
         * 登录按钮的点击事件
         */
        mSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = mInputUsername.getText().toString();
                String password = mInputPassword.getText().toString();
                String confirmPassword = mConfirmPassword.getText().toString();

                mInputUsername.setError(null);
                mInputPassword.setError(null);
                mConfirmPassword.setError(null);
                // 当用户名可用时，判断两次密码，以及注册用户跳转到主界面
                if (isAccountAvailable) {

                    if (TextUtils.isEmpty(username)) {
                        mInputUsername.setError(getString(R.string.error_username_required));
                    } else if (TextUtils.isEmpty(password)) {
                        mInputPassword.setError(getString(R.string.error_password_required));
                    } else if (!password.equals(confirmPassword)) {
                        mConfirmPassword.setError(getString(R.string.error_password_different));
                    } else {
                        /**
                         * 注册一个用户的步骤
                         */
                        User user = new User();
                        user.setUsername(username);
                        user.setPassword(password);
                        user.signUp(SigninActivity.this, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                showMessage(getString(R.string.info_signin_success));
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
                    }
                } else {
                    // 当用户名不可用时，而点击了按钮，Toast提示
                    showMessage(getString(R.string.info_account_existed));
                }
            }
        });


    }

    /**
     * 异步任务去处理用户是否存在，是否可以登录
     * 传入用户的用户名
     */
    class LoadUserTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            // 查询是否有当前这个用户名
            BmobQuery<User> query = new BmobQuery<User>();
            // 设定查询条件
            query.addWhereEqualTo("username",params[0]);
            // 执行查询方法
            query.findObjects(SigninActivity.this, new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    // 如果有这个对象，则该用户名不可用，设置标志位，设置提示消息
                    if (list.size() > 0){
                        isAccountAvailable = false;
                        mAccountInfo.setText(getString(R.string.info_account_existed));
                        mAccountInfo.setTextColor(getResources().getColor(R.color.colorHintInfo));
                        Animation animation = AnimationUtils.loadAnimation(SigninActivity.this, android.R.anim.fade_in);
                        animation.setDuration(3000);
                        mAccountInfo.setAnimation(animation);
                        mAccountInfo.setVisibility(View.VISIBLE);
                    } else {
                        // 如果没有这个对象，用户名可用，设置标志位，把提示消息的控件隐藏
                        isAccountAvailable = true;
                        Animation  animation = AnimationUtils.loadAnimation(SigninActivity.this, android.R.anim.fade_out);
                        animation.setDuration(1000);
                        mAccountInfo.setAnimation(animation);
                        mAccountInfo.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onError(int i, String s) {
                    //可能由于网络等原因而登录失败
                    showMessage("登录失败 :"+ s);
                }
            });

            return null;
        }

    }

    /**
     * 提示Toast
     * @param msg 要提示的文本
     */
    private void showMessage(String msg){
        Toast.makeText(SigninActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

}
