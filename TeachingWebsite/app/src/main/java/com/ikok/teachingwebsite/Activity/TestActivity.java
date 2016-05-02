package com.ikok.teachingwebsite.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
public class TestActivity extends AppCompatActivity {

    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mButton = (Button) findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 添加一个CourseItem
                 */
//                Course course = new Course();
//                course.setObjectId("Y1fb000P");
//                CourseItem item = new CourseItem();
//                item.setCourseItemName("Anonymous 4");
//                item.setCourse(course);
//                item.save(TestActivity.this, new SaveListener() {
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(TestActivity.this,"success",Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(int i, String s) {
//
//                    }
//                });
                /**
                 * 查询一个Course下的所有CourseItem
                 */
                BmobQuery<CourseItem> query = new BmobQuery<CourseItem>();
                //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
                Course post = new Course();
                post.setObjectId("Y1fb000P");
                // 这里 course 是bean里定义的变量名
                query.addWhereEqualTo("course",new BmobPointer(post));
                //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
//                query.include("user,post.author");
                query.findObjects(TestActivity.this, new FindListener<CourseItem>() {

                    @Override
                    public void onSuccess(List<CourseItem> object) {
                        String s = "";
                        for (CourseItem item :
                                object) {
                            s += item.getCourseItemName() + ".";
                        }

                        Toast.makeText(TestActivity.this,s,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code, String msg) {

                    }
                });
            }
        });
    }
}
