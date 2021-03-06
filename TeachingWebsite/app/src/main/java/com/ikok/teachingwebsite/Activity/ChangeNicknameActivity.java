package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/21.
 */
public class ChangeNicknameActivity extends Activity {

    /**
     * 顶部条的图片和文字
     */
    private ImageView mTopbarBackBtn;
    private TextView mTopbarTitle;
    private ImageView mTopbarDoneBtn;
    /**
     * 昵称文本
     */
    private MaterialEditText mNicknameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings_change_nickname);
//        StatusBarUtil.setColor(ChangeNicknameActivity.this, getResources().getColor(R.color.colorMain));        // 获取当前的用户
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
        final User user = BmobUser.getCurrentUser(ChangeNicknameActivity.this,User.class);
        String nickname = (String) BmobUser.getObjectByKey(ChangeNicknameActivity.this, "nickName");
//        Log.d("Anonymous","nickname is: " + nickname);
        // 初始化控件
        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);
        mTopbarDoneBtn = (ImageView) findViewById(R.id.id_top_bar_done);
        mNicknameText = (MaterialEditText) findViewById(R.id.id_settings_nickname_text);
        // 将完成按钮显示
        mTopbarDoneBtn.setVisibility(View.VISIBLE);
        // 顶部条标题显示
        mTopbarTitle.setText("修改昵称");
        // 显示已有的昵称，并把光标移动最后
        if (nickname != null){
            mNicknameText.setText(nickname);
            mNicknameText.setSelection(mNicknameText.getText().toString().length());
        }

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
                String tempNickname = mNicknameText.getText().toString();
                // 昵称不能为空字符串
                if (!tempNickname.trim().equals("") && tempNickname.length() <= 16){
                    User newUser = new User();
                    newUser.setNickName(tempNickname);
                    newUser.update(ChangeNicknameActivity.this, user.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            // 更新数据后，退出当前界面
                            Toast.makeText(ChangeNicknameActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(ChangeNicknameActivity.this,"输入有误 " + s,Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ChangeNicknameActivity.this,"输入有误，请重试",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
