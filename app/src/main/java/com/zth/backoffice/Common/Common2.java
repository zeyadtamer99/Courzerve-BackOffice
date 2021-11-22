package com.zth.backoffice.Common;

import com.zth.backoffice.Interface.RetrofitServiceCourses;
import com.zth.backoffice.Interface.RetrofitServiceRound;
import com.zth.backoffice.Retrofit.CoursesClient;
import com.zth.backoffice.Retrofit.RoundClient;

import retrofit2.Retrofit;

public class Common2 {

    private static final String BASE_URL = "https://courzerve-dev.herokuapp.com";


    public static RetrofitServiceRound getCourses() {
        return RoundClient.getClient(BASE_URL).create(RetrofitServiceRound.class);
    }


}