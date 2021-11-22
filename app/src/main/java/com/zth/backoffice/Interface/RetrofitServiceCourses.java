package com.zth.backoffice.Interface;

import com.zth.backoffice.Model.Course;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitServiceCourses {

//        @GET("/course/all/en")
//        Call<List<Course>> getCourseList();

        @GET("/course/all/en")
        Call<List<Course>> getCourseList();
}
