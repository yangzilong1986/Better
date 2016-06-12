package com.ikok.teachingwebsite.Activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ikok.teachingwebsite.CustomView.ViewPagerIndicator;
import com.ikok.teachingwebsite.Entity.Course;
import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.Fragment.CourseCommentFragment;
import com.ikok.teachingwebsite.Fragment.CourseItemDescFragment;
import com.ikok.teachingwebsite.Fragment.CourseItemListFragment;
import com.ikok.teachingwebsite.R;
import com.ikok.teachingwebsite.Util.CourseImgLoader;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/15.
 */
public class CourseItemActivity extends FragmentActivity {
    /**
     * 课程图片相关
     */
    private ImageView mImageView;
    private Course mCourse;
    private CourseImgLoader mCourseImgLoader = new CourseImgLoader();
    /**
     * ViewPager、数据源、适配器
     */
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragment = new ArrayList<Fragment>();
    /**
     * ViewPager指示器
     */
    private ViewPagerIndicator mViewPagerIndicator;
    private List<String> titles = Arrays.asList("章节","评论","描述");
    /**
     * 顶部条的图片和文字
     */
    private ImageView mTopbarBackBtn;
    private TextView mTopbarTitle;
    /**
     * 用户已经学习的课程
     */
    private List<Course> userCourseList;
    /**
     * 是否学习了该课程
     */
    private Boolean isLearned = false;
    /**
     * 进度条对话框
     */
    private ProgressDialog mProgressDialog;

    @Bind(R.id.id_course_bottom_bar_back) ImageView mCommentBtn;
    @Bind(R.id.id_course_bottom_bar_like) LikeButton mLikeBtn;
    @Bind(R.id.id_course_bottom_bar_download) ImageView mDownloadBtn;
//    @Bind(R.id.id_course_bottom_bar_share) ImageView mShareBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_course_item);
        ButterKnife.bind(this);
//        StatusBarUtil.setColor(CourseItemActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
        // 初始化控件
        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);

        final User user = BmobUser.getCurrentUser(this, User.class);

        /**
         * 先取得用户的学习课程，如果用户没有学习课程，则直接创建一个
         */
        userCourseList = user.getLearnedCourses();
//        Log.d("Anonymous","userCourseList is " + userCourseList);
        if (userCourseList == null) {
            userCourseList = new ArrayList<Course>();
//            Log.d("Anonymous","该用户没有学习任何课程");
        }

        // 当前课程对象
        Intent intent = getIntent();
        mCourse = (Course) intent.getSerializableExtra("course");

        // 顶部条标题显示
        mTopbarTitle.setText("课程详情");
        // 返回按钮事件
        mTopbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseItemActivity.this.finish();
            }
        });
        // 课程评论按钮
        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(CourseItemActivity.this,WriteCourseCommentActivity.class);
                in.putExtra("course" , mCourse);
                startActivity(in);
            }
        });
        // 下载按钮事件
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final BmobFile bmobFile = mCourse.getCourseResource();
                final File saveFile = new File(Environment.getExternalStorageDirectory() + "/Better/", bmobFile.getFilename());
                if (!saveFile.exists()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CourseItemActivity.this);
                    builder.setTitle("课程资源")
                            .setMessage("您要下载该课程的学习资料吗?请保证您的手机有一定空间可使用。")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // downloadResoruce(bmobFile);
                                    createProgressDialog(bmobFile);
                                }
                            });
                    builder.create().show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CourseItemActivity.this);
                    builder.setTitle("课程资源")
                            .setMessage("要打开该课程的资源吗?")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    openFile(CourseItemActivity.this, saveFile);
                                }
                            });
                    builder.create().show();

                }

            }
        });


        // 根据传过来的课程对象，去加载课程图片并显示
        mImageView = (ImageView) findViewById(R.id.id_item_course_img);

        final String courseImgUrl = mCourse.getCourseImg().getFileUrl(this);
        mCourseImgLoader.loadCourseImg(courseImgUrl, mImageView);

        /**
         * 每次传递进来的Course对象不一样，而值全部相等
         * 根据是否已经学习这门课，决定学习课程还是退出课程，按钮的颜色与文字
         *
         */
        // 遍历用户的学习课程，如果有该课程，按钮成为退出课程按钮，是否学习该课程的标志 isLearned 设为真
        // 遍历完，则没有该课程，添加该课程到用户的学习课程里
        int i = 0;
        for (; i < userCourseList.size(); i++) {
            if (userCourseList.get(i).getObjectId().equals(mCourse.getObjectId())) {
                mLikeBtn.setLiked(true);
                isLearned = true;
//                Toast.makeText(CourseItemActivity.this,"已经学习该课程",Toast.LENGTH_SHORT).show();
                break;
            }
        }
        if (i == userCourseList.size()) {
            userCourseList.add(mCourse);
        }

        // 学习课程按钮事件
        // 通过标志 isLearned 来处理不同的按钮事件
        mLikeBtn.setOnLikeListener(new OnLikeListener() {
            User newUser = new User();
            @Override
            public void liked(LikeButton likeButton) {
                newUser.setLearnedCourses(userCourseList);
                newUser.update(CourseItemActivity.this, user.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(CourseItemActivity.this,"用户添加课程成功",Toast.LENGTH_SHORT).show();
                        isLearned = true;
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Log.d("Anonymous", "添加课程失败: " + s + " 错误码：" + i);
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                /**
                * isLearned 为 true , 从用户课程里删除该课程
                */
//                    Log.d("Anonymous","删除该课程前的课程集合: " + userCourseList);
                // 从课程集合里删除该课程
                for (int j = 0; j < userCourseList.size(); j++) {
                    if (userCourseList.get(j).getObjectId().equals(mCourse.getObjectId())) {
                        userCourseList.remove(userCourseList.get(j));
                    }
                }
//                    Log.d("Anonymous","删除该课程前的课程集合: " + userCourseList);
                newUser.setLearnedCourses(userCourseList);
                newUser.update(CourseItemActivity.this, user.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(CourseItemActivity.this,"用户删除课程成功",Toast.LENGTH_SHORT).show();
                        isLearned = false;
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Log.d("Anonymous", "删除课程失败: " + s + " 错误码：" + i);
                    }
                });
            }
        });
        /**
         * 课程图片下的ViewPager设置，ViewPager指示器设置
         */
        // Viewpager
        mViewPager = (ViewPager) findViewById(R.id.id_item_course_viewpager);
        Bundle bundle = new Bundle();
        bundle.putSerializable("course", mCourse);
        // 课程目录Fragment
        Fragment courseItemList = new CourseItemListFragment();
        courseItemList.setArguments(bundle);
        // 课程评论Fragment
        Fragment courseComment = new CourseCommentFragment();
        courseComment.setArguments(bundle);
        // 课程描述Fragment
        Fragment courseItemDesc = new CourseItemDescFragment();
        courseItemDesc.setArguments(bundle);

        mFragment.add(courseItemList);
        mFragment.add(courseComment);
        mFragment.add(courseItemDesc);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        mViewPager.setAdapter(mAdapter);

        // tab指示器
        mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
        // 可见的tab数
        mViewPagerIndicator.setVisibleTabCount(3);
        // tab标题
        mViewPagerIndicator.setTabItemTitles(titles);
        // 为指示器设置ViewPager,并指定默认显示的tab
        mViewPagerIndicator.setViewPager(mViewPager, 0);

    }

    /**
     * 下载当前课程的资源
     *
     * @param bmobFile 课程资源
     */
    private void downloadResoruce(BmobFile bmobFile) {
        final File saveFile = new File(Environment.getExternalStorageDirectory() + "/Better/", bmobFile.getFilename());
        Log.d("Anonymous", "saveFile.getAbsolutePath() is: " + saveFile.getAbsolutePath());
        bmobFile.download(CourseItemActivity.this, saveFile, new DownloadFileListener() {
            @Override
            public void onStart() {
                super.onStart();
                Toast.makeText(CourseItemActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(Integer progress, long total) {
                super.onProgress(progress, total);
//                Log.d("Anonymous","下载进度："+progress+","+total);
//                progressBar.setProgress(progress);
                mProgressDialog.setProgress(progress);
            }

            @Override
            public void onSuccess(String s) {
                Toast.makeText(CourseItemActivity.this, "下载成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int i, String s) {
                if (i == 404) {
                    Toast.makeText(CourseItemActivity.this, "下载失败：文件未找到", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CourseItemActivity.this, "下载失败：" + s + "," + i, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * 打开文件，此处规定只能打开 mp4 文件，所以云端只能存 mp4 文件
     *
     * @param file 要打开的文件
     */
    private static void openFile(Context context, File file) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), "video/*");
        //跳转
        context.startActivity(intent);
    }

    /**
     * 创建进度条对话框
     * @param bmobFile
     */
    private void createProgressDialog(BmobFile bmobFile) {
        downloadResoruce(bmobFile);
        // 创建一个进度条对话框
        mProgressDialog = new ProgressDialog(this);
        // 设置标题
        mProgressDialog.setTitle("资源下载");
        // 设置对话框的样式
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置对话框的内容
        mProgressDialog.setMessage("进度条对话框的内容！");
        // 设置对话框进度条的最大值
        mProgressDialog.setMax(100);
        // 设置对话框进度条是否精确进度值，true为不不精确，一直在加载状态，false为精确，不是加载状态
        mProgressDialog.setIndeterminate(false);
        // 设置对话框的确定按钮事件
        mProgressDialog.setButton(ProgressDialog.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (mProgressDialog.getProgress() != 100){

                            // 当下载任务在进行时,不能关闭进度条对话框
                            try {
                                Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
                                field.setAccessible(true);
                                field.set(dialog, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            // 当下载任务完成时,可以关闭进度条对话框
                            try {
                                Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
                                field.setAccessible(true);
                                field.set(dialog, true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mProgressDialog.dismiss();
                        }



                    }
                });
        // 设置对话框是否可以被返回键关闭
        mProgressDialog.setCancelable(false);
        // 显示对话框
        mProgressDialog.show();
    }


}