package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ikok.teachingwebsite.Entity.Course;
import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.R;
import com.ikok.teachingwebsite.Util.CourseAdapter;
import com.ikok.teachingwebsite.Util.MyListener;
import com.ikok.teachingwebsite.Util.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/23.
 */
public class MyselfLearnedCourseActivity extends Activity {

    /**
     * 顶部条的图片和文字
     */
    private ImageView mTopbarBackBtn;
    private TextView mTopbarTitle;
    /**
     * 下拉刷新控件
     */
    private ListView listView;
    private PullToRefreshLayout ptrl;
    private CourseAdapter mAdapter;
    private List<Course> mCourseList = new ArrayList<Course>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myself_learned_course);
//        StatusBarUtil.setColor(MyselfLearnedCourseActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
        // 初始化控件
        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);
        // 顶部条标题显示
        mTopbarTitle.setText("学习的课程");
        // 返回按钮事件
        mTopbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 个人课程的ListView、数据源、适配器
         */
        // 下拉刷新上拉加载的布局与listview
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        ptrl.setOnRefreshListener(new MyListener());
        listView = (ListView) findViewById(R.id.content_view);

        // 显示当前用户的学习课程
        showUserCourses();

        // 点击ListView的item事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = (Course) parent.getItemAtPosition(position);
                Intent intent = new Intent(MyselfLearnedCourseActivity.this, CourseItemActivity.class);
                intent.putExtra("course",course);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 显示当前用户的学习课程
        showUserCourses();

    }

    /**
     * 显示当前用户的学习课程
     */
    private void showUserCourses() {
        // 得到当前用户
        User user = BmobUser.getCurrentUser(MyselfLearnedCourseActivity.this,User.class);
        if (user.getLearnedCourses() != null){
            mCourseList = user.getLearnedCourses();
            // 创建适配器与绑定适配器
            mAdapter = new CourseAdapter(MyselfLearnedCourseActivity.this,mCourseList);
            listView.setAdapter(mAdapter);
        }

    }
}
