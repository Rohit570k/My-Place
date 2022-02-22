package com.example.nearbyfinder.WebServices;

import com.example.nearbyfinder.Model.PlaceModel.TomResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitApi {
    /**  we will call the @GET request of the given URL and the
     * get the TomResponseModel in the response
     * @param url
     * @return
     */
    @GET
    Call<TomResponseModel> getNearByPlaces(@Url String url);
}
