package com.example.nearbyfinder.Model.PlaceModel;

import com.example.nearbyfinder.Model.TomNearbyPlaceModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TomResponseModel {

    @SerializedName("results")
    @Expose
    private List<TomNearbyPlaceModel> tomNearbyPlaceModelList;

    public List<TomNearbyPlaceModel> getTomNearbyPlaceModelList() {
        return tomNearbyPlaceModelList;
    }

    public void setTomNearbyPlaceModelList(List<TomNearbyPlaceModel> tomNearbyPlaceModelList) {
        this.tomNearbyPlaceModelList = tomNearbyPlaceModelList;
    }
}
