package com.zth.backoffice.Interface;

import com.zth.backoffice.Model.Customer;
import com.zth.backoffice.Model.Round;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface RetrofitServiceRound {

        @GET("/round/course")
        Call<List<Round>> getRoundList(@Header("courseId") String courseId);

        @GET("/round/{id}")
        Call<Round> getCustomerList(@Path("id") String roundId);

        @PUT("/round/customer/edit")
        Call<Round> putRound(@Header("roundId") String roundId, @Header("Content-Type") String Content, @Body Customer customer);

}
