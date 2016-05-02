package com.ikok.teachingwebsite.Entity;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Anonymous on 2016/4/6.
 */
public class User extends BmobUser {


    private String nickName;
    private String userIntro;
    private List<Course> learnedCourses;
    private List<Post> likedPosts;
    private BmobFile profileImg;

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public BmobFile getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(BmobFile profileImg) {
        this.profileImg = profileImg;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<Course> getLearnedCourses() {
        return learnedCourses;
    }

    public void setLearnedCourses(List<Course> learnedCourses) {
        this.learnedCourses = learnedCourses;
    }

    public List<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(List<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }
}
