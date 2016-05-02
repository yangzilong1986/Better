package com.ikok.teachingwebsite.Entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Anonymous on 2016/4/16.
 */
public class Comment extends BmobObject {

    private String commentContent;
    private Post commentPost;
    private User commentUser;
    private String commentPublishTime;

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Post getCommentPost() {
        return commentPost;
    }

    public void setCommentPost(Post commentPost) {
        this.commentPost = commentPost;
    }

    public User getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(User commentUser) {
        this.commentUser = commentUser;
    }

    public String getCommentPublishTime() {
        return commentPublishTime;
    }

    public void setCommentPublishTime(String commentPublishTime) {
        this.commentPublishTime = commentPublishTime;
    }
}
