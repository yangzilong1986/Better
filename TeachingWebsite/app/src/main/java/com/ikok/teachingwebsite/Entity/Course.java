package com.ikok.teachingwebsite.Entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Anonymous on 2016/4/11.
 */
public class Course extends BmobObject {

    private String courseName;
    private Integer courseLearnTime;
    private String  courseUpdateTime;
    private BmobFile courseImg;
    private String courseDesc;
    private BmobFile courseResource;

    public BmobFile getCourseResource() {
        return courseResource;
    }

    public void setCourseResource(BmobFile courseResource) {
        this.courseResource = courseResource;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseLearnTime() {
        return courseLearnTime;
    }

    public void setCourseLearnTime(Integer courseLearnTime) {
        this.courseLearnTime = courseLearnTime;
    }

    public String getCourseUpdateTime() {
        return courseUpdateTime;
    }

    public void setCourseUpdateTime(String courseUpdateTime) {
        this.courseUpdateTime = courseUpdateTime;
    }

    public BmobFile getCourseImg() {
        return courseImg;
    }

    public void setCourseImg(BmobFile courseImg) {
        this.courseImg = courseImg;
    }

}
