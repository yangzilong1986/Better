package com.ikok.teachingwebsite.Entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Anonymous on 2016/4/15.
 */
public class CourseItem extends BmobObject {

    private String courseItemName;
    private Course course;


    public String getCourseItemName() {
        return courseItemName;
    }

    public void setCourseItemName(String courseItemName) {
        this.courseItemName = courseItemName;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
