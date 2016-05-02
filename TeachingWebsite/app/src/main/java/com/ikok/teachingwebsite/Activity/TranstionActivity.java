package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.ikok.teachingwebsite.R;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/29.
 */
public class TranstionActivity extends Activity {

    private Boolean isFirstIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_transtion);
//        StatusBarUtil.setColor(TranstionActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));


        final SharedPreferences sharedPreferences = getSharedPreferences("is_first_in_data",MODE_PRIVATE);
        isFirstIn = sharedPreferences.getBoolean("isFirstIn",true);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent intent = null;

                if (isFirstIn) {
                    intent = new Intent(TranstionActivity.this, GuideActivity.class);
                    TranstionActivity.this.startActivity(intent);
                    TranstionActivity.this.finish();
                } else {
                    intent = new Intent(TranstionActivity.this, LoginActivity.class);
                    TranstionActivity.this.startActivity(intent);
                    TranstionActivity.this.finish();
                }
            }
        }, 2000);


    }
}
