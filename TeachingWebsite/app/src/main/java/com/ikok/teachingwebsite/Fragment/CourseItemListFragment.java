package com.ikok.teachingwebsite.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikok.teachingwebsite.Entity.Course;
import com.ikok.teachingwebsite.Entity.CourseItem;
import com.ikok.teachingwebsite.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Anonymous on 2016/4/15.
 */
public class CourseItemListFragment extends Fragment {

    private View mView;
    private Course mCourse;
    private TextView mTitle;
    private TextView mCourseItemList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_course_item_viewpager1,container,false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTitle = (TextView) mView.findViewById(R.id.id_item_course_list_title);
        mCourseItemList = (TextView) mView.findViewById(R.id.id_item_course_list);

        // 接收course参数
        Bundle bundle = getArguments();
        mCourse = (Course) bundle.getSerializable("course");
//        Log.d("Anonymous","Fragment getObjectId is: " + mCourse.getObjectId());

        mTitle.setText(mCourse.getCourseName());

        /**
         * 查询一个Course下的所有CourseItem
         */
        BmobQuery<CourseItem> query = new BmobQuery<CourseItem>();
        query.addWhereEqualTo("course",new BmobPointer(mCourse));
        query.findObjects(getContext(), new FindListener<CourseItem>() {

            @Override
            public void onSuccess(List<CourseItem> object) {
                String s = "";
                for (CourseItem item : object) {
                    s += (object.indexOf(item)+1) +" " + item.getCourseItemName() + "\n";
                }
                mCourseItemList.setText(s);
//                Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int code, String msg) {

            }
        });

    }



}
