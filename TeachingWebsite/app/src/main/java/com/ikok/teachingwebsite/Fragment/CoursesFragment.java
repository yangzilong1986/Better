package com.ikok.teachingwebsite.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ikok.teachingwebsite.Activity.CourseItemActivity;
import com.ikok.teachingwebsite.Activity.SearchActivity;
import com.ikok.teachingwebsite.Entity.Course;
import com.ikok.teachingwebsite.R;
import com.ikok.teachingwebsite.Util.CourseAdapter;
import com.ikok.teachingwebsite.Util.MyListener;
import com.ikok.teachingwebsite.Util.PullToRefreshLayout;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Anonymous on 2016/4/10.
 */
public class CoursesFragment extends Fragment {
    /**
     * 下拉刷新控件
     */
    private ListView listView;
    private PullToRefreshLayout ptrl;
    private boolean isFirstIn = true;
    /**
     * 数据源，适配器
     */
    private List<Course> mCourseList = new ArrayList<Course>();
    private CourseAdapter mAdapter;

    private String json = null;
    /**
     * 界面视图
     */
    private View view = null;
    /**
     * 广告轮播控件
     */
    private CarouselView mCarouselView;
    /**
     * 搜索图片
     */
    private ImageView mSearchBtn;
    /**
     * 图片资源数组
     */
    private int[] mAdImgs = {R.drawable.img_ad1,R.drawable.img_ad2,R.drawable.img_ad3,
            R.drawable.img_ad4,R.drawable.img_ad5};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * 初始化 Bmob SDK
         */
        Bmob.initialize(getContext(), "6672f54e9508f8fad63daa61f2b59c9c");
        view = inflater.inflate(R.layout.activity_courses,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 下拉刷新上拉加载的布局与listview
        ptrl = ((PullToRefreshLayout) view.findViewById(R.id.refresh_view));
        ptrl.setOnRefreshListener(new MyListener());
        listView = (ListView) view.findViewById(R.id.content_view);

        mSearchBtn = (ImageView) view.findViewById(R.id.id_search_btn);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        // 加载所有课程,并显示
        LoadCourse();

        // 点击ListView的item事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = (Course) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), CourseItemActivity.class);
                intent.putExtra("course",course);
                startActivity(intent);
            }
        });

        // 广告轮播控件
//        mCarouselView = (CarouselView) view.findViewById(R.id.id_carouselView);
//        if (mCarouselView != null) {
//            mCarouselView.setPageCount(mAdImgs.length);
//            mCarouselView.setImageListener(imageListener);
//        }

    }

    /**
     * 图片轮播的图片监听器
     */
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(mAdImgs[position]);
        }
    };


    /**
     * 加载课程的异步任务
     */
    private void LoadCourse(){
        // TODO 进行网络操作前都要先判断网络状态是否可用
        // 创建查询
        BmobQuery<Course> query = new BmobQuery<Course>();
        // 查询的结果排序方式，按更新时间倒序排列
        query.order("-courseUpdateTime");
        // 查询的缓存方式
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        // 查询及事件监听器
        query.findObjects(getContext(), new FindListener<Course>() {

            @Override
            public void onSuccess(List<Course> courses) {
                // 创建适配器与绑定适配器
                mAdapter = new CourseAdapter(getContext(),courses);
                listView.setAdapter(mAdapter);
            }

            @Override
            public void onError(int code, String arg0) {

            }
        });
    }

    /**
     * 解析Json数组
     * @param jsonData Json
     * @return Course集合
     */
    private List<Course> parseJsonArray(String jsonData){
        List<Course> courseList = new ArrayList<Course>();
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                Course course = new Course();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                course.setCourseName(jsonObject.getString("courseName"));
                course.setCourseLearnTime(jsonObject.getInt("courseLearnTime"));
                course.setCourseUpdateTime(jsonObject.getString("courseUpdateTime"));
                JSONObject courseImg = jsonObject.getJSONObject("courseImg");
                courseList.add(course);
                Log.d("Anonymous", "parseJsonArray: " + course.getCourseName() + " " + course.getCourseLearnTime()
                                    + " " +  course.getCourseUpdateTime());
                Log.d("Anonymous"," courseList size: " + courseList.size());
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return courseList;
    }


}




