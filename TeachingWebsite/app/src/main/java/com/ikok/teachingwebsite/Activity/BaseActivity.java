package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.ikok.teachingwebsite.R;
import com.ikok.teachingwebsite.Util.ActivityCollector;

import cn.bmob.v3.Bmob;


public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /**
         * 初始化 Bmob SDK
         */
        Bmob.initialize(this, "6672f54e9508f8fad63daa61f2b59c9c");

        Log.d("当前所在的Activity:", getClass().getSimpleName());

        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    //    protected void setUpToolbarWithTitle(String title, boolean hasBackButton){
//        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_actionbar);
//        setSupportActionBar(toolBar);
//        if(getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(title);
//            getSupportActionBar().setDisplayShowHomeEnabled(hasBackButton);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(hasBackButton);
//        }
//    }

    protected void enterFromBottomAnimation(){
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
    }

    protected void exitToBottomAnimation(){
        overridePendingTransition(R.anim.activity_no_animation, R.anim.activity_close_translate_to_bottom);
    }

    /**
     * 判断是否有网络连接
     * @param activity 当前活动
     * @return 是否有网络连接
     */
    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null){
            return false;
        } else {

            NetworkInfo[] networkInfos = manager.getAllNetworkInfo();
            if (networkInfos != null && networkInfos.length > 0){
                for (int i = 0; i < networkInfos.length; i++) {
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * toast 弹出提示
     * @param msg 提示内容
     */
    public void toast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

}
