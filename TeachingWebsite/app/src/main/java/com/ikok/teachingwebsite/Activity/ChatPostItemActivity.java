package com.ikok.teachingwebsite.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ikok.teachingwebsite.CustomView.CustomScrollView;
import com.ikok.teachingwebsite.Entity.Comment;
import com.ikok.teachingwebsite.Entity.Post;
import com.ikok.teachingwebsite.Entity.User;
import com.ikok.teachingwebsite.R;
import com.ikok.teachingwebsite.Util.CommentAdapter;
import com.ikok.teachingwebsite.Util.UserImgLoader;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Anonymous on 2016/4/16.
 */
public class ChatPostItemActivity extends Activity {

    private TextView mPostTitle;
    private CircleImageView mUserImg;
    private TextView mUserName;
    private TextView mPostContent;
    private TextView mPostTime;
    private CustomScrollView mScrollView;
    /**
     * 评论列表的适配器，数据源，ListView
     */
    private CommentAdapter mAdapter;
    private ListView mCommentListView;
    /**
     * 顶部条的图片和文字
     */
    private ImageView mTopbarBackBtn;
    private TextView mTopbarTitle;
    /**
     * 底部的评论、收藏、分享
     */
    private ImageView mCommentBtn;
    private LikeButton mLikeButton;
    private ImageView mShareBtn;
    /**
     * 用户是否收藏了该课程
     */
    private Boolean isLiked = false;
    private List<Post> mPostList;
    /**
     * 当前文章
     */
    private Post mPost;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat_post_item);
//        StatusBarUtil.setColor(ChatPostItemActivity.this, getResources().getColor(R.color.colorMain));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorMain));
        // 获取传递过来的post
        Intent intent = this.getIntent();
        mPost = (Post) intent.getSerializableExtra("post");
//        Log.d("Anonymous","item view post.getObjectId() is: " + mPost.getObjectId());

        // 初始化控件
        initViews();

        // 得到当前用户
        final User user = BmobUser.getCurrentUser(this,User.class);

        /**
         * 先取得用户的收藏文章，如果用户没有收藏该文章，则直接创建一个
         */
        mPostList = user.getLikedPosts();
        if (mPostList == null){
            mPostList = new ArrayList<Post>();
        }
        /**
         * 每次传递进来的Course对象不一样，而值全部相等
         * 根据是否已经学习这门课，决定学习课程还是退出课程，按钮的颜色与文字
         *
         */
        // 遍历用户的学习课程，如果有该课程，按钮成为退出课程按钮，是否学习该课程的标志 isLiked 设为真
        // 遍历完，则没有该课程，添加该课程到用户的学习课程里
        int i = 0;
        for ( ; i < mPostList.size(); i++){
            if (mPostList.get(i).getObjectId().equals(mPost.getObjectId())){
                mLikeButton.setLiked(true);
                isLiked = true;
//                Toast.makeText(ChatPostItemActivity.this,"已经学习该课程",Toast.LENGTH_SHORT).show();
                break;
            }
        }
        if (i == mPostList.size()){
            mPostList.add(mPost);
        }

        // 设置显示
        mPostTitle.setText(mPost.getPostTitle());
        mPostContent.setText(mPost.getPostContent());
        mPostTime.setText(mPost.getPostPublishTime());
        // 顶部条标题显示
        mTopbarTitle.setText("文章详情");

        User postAuthor = mPost.getPostAuthor();
        mUserName.setText(postAuthor.getUsername());

//        Log.d("Anonymous","user is: " + postAuthor.getObjectId() + " " + postAuthor.getUsername());
        new LoadPostItemTask().execute(mPost);

        if (postAuthor.getProfileImg() != null){
            String url = postAuthor.getProfileImg().getFileUrl(this);
            new UserImgLoader(mUserImg).execute(url);
        }

        // 控件绑定监听器
        initEvents();


        /**
         * 收藏文章按钮事件
         */
        mLikeButton.setOnLikeListener(new OnLikeListener() {
            User newUser = new User();
            @Override
            public void liked(LikeButton likeButton) {
                /**
                 * isLiked 为 false ，添加该文章到用户文章里
                 */
                if (!isLiked){
                    newUser.setLikedPosts(mPostList);
                    newUser.update(ChatPostItemActivity.this, user.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(ChatPostItemActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                            // 添加成功后，修改按钮样式
                            mLikeButton.setLiked(true);
                            isLiked = true;
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.d("Anonymous","添加文章失败: " + s + " 错误码：" + i);
                        }
                    });
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (isLiked){
                    /**
                     * isLiked 为 true , 从用户文章里删除该文章
                     */
//                    Log.d("Anonymous","删除该文章前的文章集合: " + mPostList);
                    // 从文章集合里删除该文章
                    for (int j = 0; j < mPostList.size(); j++){
                        if (mPostList.get(j).getObjectId().equals(mPost.getObjectId())){
                            mPostList.remove(mPostList.get(j));
                        }
                    }
//                    Log.d("Anonymous","删除该文章前的文章集合: " + mPostList);
                    newUser.setLikedPosts(mPostList);
                    newUser.update(ChatPostItemActivity.this, user.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(ChatPostItemActivity.this,"取消收藏成功",Toast.LENGTH_SHORT).show();
                            // 删除成功后，修改按钮样式
                            mLikeButton.setLiked(false);
                            isLiked = false;
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.d("Anonymous","删除文章失败: " + s + " 错误码：" + i);
                        }
                    });
                }
            }
        });

        /**
         * 底部评论按钮事件
         */
        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ChatPostItemActivity.this,WritePostCommentActivity.class);
                in.putExtra("post" , mPost);
                startActivity(in);
            }
        });

    }

    /**
     * 初始化视图
     */
    private void initViews() {
        mPostTitle = (TextView) findViewById(R.id.id_chat_post_item_title);
        mUserImg = (CircleImageView) findViewById(R.id.id_chat_post_item_user_img);
        mUserName = (TextView) findViewById(R.id.id_chat_post_item_author_name);
        mPostContent = (TextView) findViewById(R.id.id_chat_post_item_content);
        mPostTime = (TextView) findViewById(R.id.id_chat_post_item_time);
        mCommentListView = (ListView) findViewById(R.id.id_chat_post_item_comment_list);
        mScrollView = (CustomScrollView) findViewById(R.id.id_post_item_scrollview);
        mTopbarBackBtn = (ImageView) findViewById(R.id.id_top_bar_img);
        mTopbarTitle = (TextView) findViewById(R.id.id_top_bar_title);
        mCommentBtn = (ImageView) findViewById(R.id.id_post_bottom_bar_back);
        mLikeButton = (LikeButton) findViewById(R.id.id_post_bottom_bar_like);
//        mShareBtn = (ImageView) findViewById(R.id.id_post_bottom_bar_share);
    }

    /**
     * 控件监听器事件
     */
    private void initEvents() {

        // 顶部返回按钮事件
        mTopbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 底部分享按钮事件
//        mShareBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

    }

    /**
     * 依据子项设置ListView的高度
     * @param commentListView 目标ListView
     */
    private void setListViewHeightBasedOnChildren(ListView commentListView) {
        // 获取ListView对应的Adapter
        CommentAdapter listAdapter = (CommentAdapter) commentListView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, commentListView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = commentListView.getLayoutParams();
        params.height = totalHeight+ (commentListView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        commentListView.setLayoutParams(params);

    }

    class LoadPostItemTask extends AsyncTask<Post,Void,Void> {

        @Override
        protected Void doInBackground(Post... params) {

            BmobQuery<Comment> query = new BmobQuery<Comment>();
            //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
//            Post post = new Post();
//            post.setObjectId("ESIt3334");
            query.addWhereEqualTo("commentPost",new BmobPointer(params[0]));
            //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
            query.include("commentUser,commentPost.postAuthor");
            query.findObjects(ChatPostItemActivity.this, new FindListener<Comment>() {

                @Override
                public void onSuccess(List<Comment> object) {
                    mAdapter = new CommentAdapter(ChatPostItemActivity.this,object);
                    mCommentListView.setAdapter(mAdapter);
                    setListViewHeightBasedOnChildren(mCommentListView);
                    // 在计算好ListView的高度之后，页面会自动加载到ListView的上方，而不是页面的上方
                    // 通过ScrollView设置打开页面的起始位置
                    mScrollView.scrollTo(0,0);
                }

                @Override
                public void onError(int code, String msg) {
                    Log.d("Anonymous","ChatPostItemActivity 错误: " + msg + " 错误码：" + code);
                }
            });

            return null;
        }
    }


    /**
     * 当用户评论完，重新加载一次评论，更新该文章
     */
    @Override
    protected void onResume() {
        super.onResume();
        new LoadPostItemTask().execute(mPost);
    }

}
