package com.ikok.teachingwebsite.Entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Anonymous on 2016/4/21.
 */
public class Feedback extends BmobObject {

    private String content;
    private String contact;
    private User feedbackUser;

    public User getFeedbackUser() {
        return feedbackUser;
    }

    public void setFeedbackUser(User feedbackUser) {
        this.feedbackUser = feedbackUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
