package com.example.administrator.rxjava_retrofit_regisrer;


import io.reactivex.Observable;
import retrofit2.http.GET;

public interface GetRequest_Interface {
    //请求1
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20register")
    Observable<Translation_1> getCall();

    //请求2
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20login")
    Observable<Translation_2> getCall_2();

}
