package com.ikok.teachingwebsite.Entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Anonymous on 2016/5/10.
 */
public class CourseComment extends BmobObject {

    private String commentContent;
    private Course commentCourse;
    private User commentUser;
    private String commentPublishTime;

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Course getCommentCourse() {
        return commentCourse;
    }

    public void setCommentCourse(Course commentCourse) {
        this.commentCourse = commentCourse;
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
