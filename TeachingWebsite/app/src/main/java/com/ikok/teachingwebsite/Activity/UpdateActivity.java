package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.ikok.teachingwebsite.R;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/22.
 */
public class UpdateActivity extends Activity {

    /**
     * 顶部条的图片和文字
     */
    private ImageView mTopbarBackBtn;
    private TextView mTopbarTitle;

    private NumberProgressBar mProgressBar;
    private TextView mUpdateResult;

    private IncreaseProgressTask mProgressTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings_update);
//        StatusBarUtil.setColor(UpdateActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
        // 初始化控件
        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);
        mProgressBar = (NumberProgressBar) findViewById(R.id.id_settings_progressbar);
        mUpdateResult = (TextView) findViewById(R.id.id_settings_update_result);
        // 顶部条标题显示
        mTopbarTitle.setText("检查更新");
        // 返回按钮事件
        mTopbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mProgressTask = new IncreaseProgressTask();
        mProgressTask.execute();

    }

    class IncreaseProgressTask extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i <= 100; i++) {
                if (isCancelled()){
                    break;
                }
                if ((i%13) == 0){
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                publishProgress(i);

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (isCancelled()){
                return ;
            }
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mUpdateResult.setText("当前为最新版本");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mProgressTask != null && mProgressTask.getStatus() == AsyncTask.Status.RUNNING){
            // cancel 方法只是将对应的AsyncTask标记为cancel状态，并不是真正的取消线程的执行
            mProgressTask.cancel(true);
        }
    }
}
