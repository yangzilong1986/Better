package com.ikok.teachingwebsite.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikok.teachingwebsite.Entity.Course;
import com.ikok.teachingwebsite.R;

/**
 * Created by Anonymous on 2016/4/15.
 */
public class CourseItemDescFragment extends Fragment {

    private View mView;
    private TextView mTextView;
    private TextView mTitle;
    private Course mCourse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_course_item_viewpager2,container,false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTextView = (TextView) mView.findViewById(R.id.id_item_course_desc);
        mTitle = (TextView) mView.findViewById(R.id.id_item_course_desc_title);
        // 接收course参数
        Bundle bundle = getArguments();
        mCourse = (Course) bundle.getSerializable("course");

        mTitle.setText(mCourse.getCourseName());
        mTextView.setText(mCourse.getCourseDesc());
    }
}
