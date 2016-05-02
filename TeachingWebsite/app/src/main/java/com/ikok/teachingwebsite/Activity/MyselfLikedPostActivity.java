package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ikok.teachingwebsite.Entity.Post;
import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.R;
import com.ikok.teachingwebsite.Util.MyListener;
import com.ikok.teachingwebsite.Util.PostAdapter;
import com.ikok.teachingwebsite.Util.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/24.
 */
public class MyselfLikedPostActivity extends Activity {

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
    /**
     * 适配器、数据源
     */
    private PostAdapter mAdapter;
    private List<Post> mPostList = new ArrayList<Post>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myself_learned_course);
//        StatusBarUtil.setColor(MyselfLikedPostActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
        // 初始化控件
        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);
        // 顶部条标题显示
        mTopbarTitle.setText("收藏的文章");
        // 返回按钮事件
        mTopbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * ListView绑定适配器
         */
        // 下拉刷新上拉加载的布局与listview
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        ptrl.setOnRefreshListener(new MyListener());
        listView = (ListView) findViewById(R.id.content_view);
        // 显示当前用户的收藏文章
        showUserPosts();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 带参数跳转到具体的文章界面
                Post post = (Post) parent.getItemAtPosition(position);
                ;
                Log.d("Anonymous","post username is: " + post.getPostAuthor().getUsername());
                Log.d("Anonymous","click post.getObjectId() is: " + post.getObjectId());
                Intent intent = new Intent(MyselfLikedPostActivity.this, ChatPostItemActivity.class);
                intent.putExtra("post",post);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        // 显示当前用户的收藏文章
        showUserPosts();
    }

    /**
     * 显示用户收藏的文章
     */
    private void showUserPosts() {
        // 得到当前用户
        User user = BmobUser.getCurrentUser(MyselfLikedPostActivity.this,User.class);
        if (user.getLikedPosts() != null){
            mPostList = user.getLikedPosts();
            // 创建适配器与绑定适配器
            mAdapter = new PostAdapter(MyselfLikedPostActivity.this,mPostList);
            listView.setAdapter(mAdapter);
        }

    }
}
