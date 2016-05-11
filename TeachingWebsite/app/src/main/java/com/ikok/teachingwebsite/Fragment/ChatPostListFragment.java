package com.ikok.teachingwebsite.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ikok.teachingwebsite.Activity.ChatPostItemActivity;
import com.ikok.teachingwebsite.Entity.Post;
import com.ikok.teachingwebsite.R;
import com.ikok.teachingwebsite.Util.MyListener;
import com.ikok.teachingwebsite.Util.PostAdapter;
import com.ikok.teachingwebsite.Util.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Anonymous on 2016/4/16.
 */
public class ChatPostListFragment extends Fragment {

    private View mView;
    /**
     * 下拉刷新控件
     */
    private PullToRefreshLayout mRefreshLayout;
    private ListView mListView;

    /**
     * 数据源、适配器
     */
    private List<Post> mPostList = new ArrayList<Post>();
    private PostAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * 初始化 Bmob SDK
         */
        Bmob.initialize(getContext(), "6672f54e9508f8fad63daa61f2b59c9c");
        mView = inflater.inflate(R.layout.activity_chat_viewpager1,container,false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRefreshLayout = (PullToRefreshLayout) mView.findViewById(R.id.id_chat_refresh_layout);
        mRefreshLayout.setOnRefreshListener(new MyListener());
        mListView = (ListView) mView.findViewById(R.id.id_chat_viewpager1_list);

        mListView.setDivider(new ColorDrawable(Color.LTGRAY));
        mListView.setDividerHeight(1);

        LoadPost();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 带参数跳转到具体的文章界面
                Post post = (Post) parent.getItemAtPosition(position);
//                Log.d("Anonymous","post username is: " + post.getPostAuthor().getUsername());
//                Log.d("Anonymous","click post.getObjectId() is: " + post.getObjectId());
                Intent intent = new Intent(getContext(), ChatPostItemActivity.class);
                intent.putExtra("post",post);
                startActivity(intent);

            }
        });

    }

    /**
     * 加载所有文章
     */
    private void LoadPost(){
        // TODO 进行网络操作前都要先判断网络状态是否可用
        // 创建查询
        BmobQuery<Post> query = new BmobQuery<Post>();
        // 查询的结果排序方式，按更新时间倒序排列
        query.order("-updatedAt");
        // 查询的缓存方式
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        // 查询及事件监听器
        // 查询的时候把用户也查询出来，跳转到具体项的时候可以直接使用用户表的操作
        query.include("postAuthor");
        query.findObjects(getContext(), new FindListener<Post>() {
            @Override
            public void onSuccess(List<Post> list) {
//                    Log.d("Anonymous","Post list is: " + list.size());
                mAdapter = new PostAdapter(getContext(),list);
                mListView.setAdapter(mAdapter);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    /**
     * 当用户写完文章后 ，立即刷新数据源，更新界面
     */
    @Override
    public void onResume() {
        super.onResume();
        LoadPost();
    }
}
