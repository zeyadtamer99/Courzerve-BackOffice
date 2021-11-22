package com.zth.backoffice.Model;

import java.util.ArrayList;

public class Course {
    private String courseId;
    private String title;
    private String coursePhotoURL;

    public Course(String courseId, String coursePhotoURL, String title) {

        this.courseId = courseId;
        this.title = title;
        this.coursePhotoURL = coursePhotoURL;
    }

    public String getTitle() {
        return title;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCoursePhotoURL() {
        return coursePhotoURL;
    }
}

