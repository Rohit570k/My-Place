package com.example.nearbyfinder.WebServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//To Retrofit Client for the requesting the data
public class RetrofitClient {

    private static Retrofit retrofit=null;
    private static  final String BASE_URL ="https://api.tomtom.com";

    public static Retrofit getRetrofitClient(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
