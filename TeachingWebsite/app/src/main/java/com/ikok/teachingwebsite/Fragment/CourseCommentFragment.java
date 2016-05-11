package com.ikok.teachingwebsite.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ikok.teachingwebsite.Entity.Course;
import com.ikok.teachingwebsite.Entity.CourseComment;
import com.ikok.teachingwebsite.R;
import com.ikok.teachingwebsite.Util.CourseCommentAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Anonymous on 2016/5/10.
 */
public class CourseCommentFragment extends Fragment {

    private View mView = null;
    private RelativeLayout mNoDataLayout;
    private ListView mCommentList;
    private Course mCourse;
    private CourseCommentAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_listview,container,false);
        mNoDataLayout = (RelativeLayout) mView.findViewById(R.id.id_no_data_layout);
        mCommentList = (ListView) mView.findViewById(R.id.id_course_item_comment_list);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 接收course参数
        Bundle bundle = getArguments();
        mCourse = (Course) bundle.getSerializable("course");
        /**
         * 查询一个Course下的所有CourseItem
         */
        BmobQuery<CourseComment> query = new BmobQuery<CourseComment>();
        query.order("-commentPublishTime");
        query.addWhereEqualTo("commentCourse",new BmobPointer(mCourse));
        query.include("commentUser");
        query.findObjects(getContext(), new FindListener<CourseComment>() {
            @Override
            public void onSuccess(List<CourseComment> list) {
                if (list.toString().equals("[]")){
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    mCommentList.setVisibility(View.GONE);
                } else {
                    if (mNoDataLayout.getVisibility() == View.VISIBLE){
                        mNoDataLayout.setVisibility(View.GONE);
                    }
                    if (mCommentList.getVisibility() == View.GONE){
                        mCommentList.setVisibility(View.VISIBLE);
                    }
                    mAdapter = new CourseCommentAdapter(getContext(),list);
                    mCommentList.setAdapter(mAdapter);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d("Anonymous","错误: " + s + " 错误码："+ i);
            }
        });
    }
}
