package com.ikok.teachingwebsite.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikok.teachingwebsite.CustomView.MaterialCheckBox;
import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.listener.SaveListener;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/7.
 */
public class LoginActivity extends BaseActivity {

    /**
     * UI 控件
     */
    private MaterialCheckBox mMaterialCheckBox;
    private MaterialEditText mEmailView;
    private EditText mPasswordView;
    private Button mEmailSignInButton;
    private TextView mCheckText;
    /**
     * 账号密码
     */
    private String username = null;
    private String password = null;
    /**
     * 账号是否记住密码
     */
    private boolean isAccountSaved = false;
    /**
     * 顶部条的图片和文字
     */
    private ImageView mTopbarBackBtn;
    private TextView mTopbarTitle;
    private TextView mTopbarSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        StatusBarUtil.setColor(LoginActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));

        /**
         * 初始化控件
         */
        mCheckText = (TextView) findViewById(R.id.checkbox_text);
        mEmailView = (MaterialEditText) findViewById(R.id.email_text);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mMaterialCheckBox = (MaterialCheckBox) findViewById(R.id.id_checkbox);

        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);
        mTopbarSign = (TextView) findViewById(R.id.id_top_bar_sign);

        /**
         * 是否保存账户，保存了则自动填充用户名和密码
         */
        SharedPreferences sharedPreferences = getSharedPreferences("is_save_account", MODE_PRIVATE);
        isAccountSaved = sharedPreferences.getBoolean("isSaveAccount",false);
        if (isAccountSaved){
            mEmailView.setText(sharedPreferences.getString("username",""));
            mEmailView.setSelection(mEmailView.getText().toString().length());
            mPasswordView.setText(sharedPreferences.getString("password",""));
            mMaterialCheckBox.setChecked(true);
        }

        // 顶部条标题显示
        mTopbarTitle.setText("登录");
        // 返回按钮事件
        mTopbarBackBtn.setVisibility(View.GONE);
        // 顶部登录注册按钮
        mTopbarSign.setVisibility(View.VISIBLE);
        mTopbarSign.setText("注册");
        mTopbarSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SigninActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 记住账号的文本点击事件，同样可以控制复选框的选中状态
         */
        mCheckText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMaterialCheckBox.isChecked()){
                    mMaterialCheckBox.setChecked(false);
                } else {
                    mMaterialCheckBox.setChecked(true);
                }
            }
        });
        /**
         * 登录按钮的点击事件
         */
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = mEmailView.getText().toString();
                password = mPasswordView.getText().toString();
                if (mMaterialCheckBox.isChecked()){
                    saveAccount(username,password);
                } else {
                    dontSaveAccount();
                }
                mEmailView.setError(null);
                mPasswordView.setError(null);
                if (TextUtils.isEmpty(username)){
                    mEmailView.setError(getString(R.string.error_field_required));
                } else if (TextUtils.isEmpty(password)){
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                } else {
                    /**
                     * 先判断网络状态是否可用
                     * 把根据名字查询的方法提取出来复用
                     * 用户登录，检索用户是否存在
                     * 用户存在，并且账号密码均正确才可登录
                     * 用户不存在时，提示
                     */
                    if (isNetworkAvailable(LoginActivity.this)){
                        new LoadUserTask().execute();
                    } else {
                        toast("网络错误，请检查您的网络连接");
                    }
                }
            }
        });

    }

    /**
     * 异步任务去处理用户是否存在，是否可以登录
     * 传入用户的用户名
     */
    class LoadUserTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.login(LoginActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    // 登录成功，跳转到主界面
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    // 匹配错误码，提示不同内容
                    if (i == 101){
                        toast("登录失败:用户名或密码错误");
                    } else if (i == 9016) {
                        toast("网络错误，请检查您的网络连接");
                    } else {
                        toast("登录失败 :"+ s + " " + i);
                        Log.d("Anonymous","登录失败 :"+ s + " " + i);
                    }

                }
            });

            return null;
        }


    }

    /**
     * 保存用户名和密码
     */
    private void saveAccount(String username,String password){

//        Log.d("Anonymous"," is: " + username + " " + password);

        SharedPreferences preferences = getSharedPreferences("is_save_account",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isSaveAccount",true);
        editor.putString("username",username);
        editor.putString("password",password);
        editor.commit();
    }

    /**
     * 不保存用户名和密码
     */
    private void dontSaveAccount(){
        SharedPreferences preferences = getSharedPreferences("is_save_account",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
