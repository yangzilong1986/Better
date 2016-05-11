package com.ikok.teachingwebsite.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ikok.teachingwebsite.Entity.Course;
import com.ikok.teachingwebsite.R;
import com.ikok.teachingwebsite.Util.CourseAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/5/10.
 */
public class SearchActivity extends BaseActivity {

    @Bind(R.id.id_top_bar_img) ImageView mBackBtn;
    @Bind(R.id.id_search_content) EditText mSearchContent;
    @Bind(R.id.id_search_btn) ImageView mSearchBtn;
    @Bind(R.id.is_search_result) ListView mSearchResultList;
    @Bind(R.id.id_no_data_layout) RelativeLayout mNoDataLayout;
    private CourseAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
        // 初始化所有监听器
        initEvents();
    }

    private void initEvents() {
        // 退出按钮
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
        // 搜索按钮
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchContent = mSearchContent.getText().toString();
                if (!TextUtils.isEmpty(searchContent)){
                    BmobQuery<Course> query = new BmobQuery<Course>();
                    query.order("-courseUpdateTime");
                    query.addWhereContains("courseName", searchContent);
                    query.findObjects(SearchActivity.this, new FindListener<Course>() {
                        @Override
                        public void onSuccess(List<Course> list) {
                            if (list.toString().equals("[]")){
                                mNoDataLayout.setVisibility(View.VISIBLE);
                                mSearchResultList.setVisibility(View.GONE);
                            } else {
                                if (mNoDataLayout.getVisibility() == View.VISIBLE){
                                    mNoDataLayout.setVisibility(View.GONE);
                                }
                                if (mSearchResultList.getVisibility() == View.GONE){
                                    mSearchResultList.setVisibility(View.VISIBLE);
                                }
                                mAdapter = new CourseAdapter(SearchActivity.this,list);
                                mSearchResultList.setAdapter(mAdapter);
                            }
                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.d("Anonymous","错误: " + s + " 错误码："+ i);
                        }
                    });
                } else {
                    toast("请输入要搜索的课程");
                }

            }
        });
        // 查询的结果点击进入课程详情
        mSearchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = (Course) parent.getItemAtPosition(position);
                Intent intent = new Intent(SearchActivity.this, CourseItemActivity.class);
                intent.putExtra("course",course);
                startActivity(intent);
            }
        });
    }
}
