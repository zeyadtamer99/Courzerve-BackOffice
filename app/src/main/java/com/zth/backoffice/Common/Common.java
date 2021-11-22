package com.zth.backoffice.Common;

import com.zth.backoffice.Interface.RetrofitServiceCourses;
import com.zth.backoffice.Interface.RetrofitServiceRound;
import com.zth.backoffice.Retrofit.CoursesClient;
import com.zth.backoffice.Retrofit.RoundClient;

import retrofit2.Retrofit;

public class Common {

    private static final String BASE_URL = "https://courzerve-dev.herokuapp.com";

    public static RetrofitServiceCourses getCourses(){
        return CoursesClient.getClient(BASE_URL).create(RetrofitServiceCourses.class);
    }

    public static RetrofitServiceRound getRound(){
        return RoundClient.getClient(BASE_URL).create(RetrofitServiceRound.class);
    }

    public static RetrofitServiceRound putRound(){
        return RoundClient.getClient(BASE_URL).create(RetrofitServiceRound.class);
    }

}
