package com.ikok.teachingwebsite.Entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Anonymous on 2016/4/16.
 */
public class Post extends BmobObject {

    private String postTitle;
    private String postContent;
    private String postPublishTime;
    private User postAuthor;

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostPublishTime() {
        return postPublishTime;
    }

    public void setPostPublishTime(String postPublishTime) {
        this.postPublishTime = postPublishTime;
    }

    public User getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(User postAuthor) {
        this.postAuthor = postAuthor;
    }

}
