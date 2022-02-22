package com.example.nearbyfinder.Model;

import com.example.nearbyfinder.Model.PlaceModel.AddressModel;
import com.example.nearbyfinder.Model.PlaceModel.PoiModel;
import com.example.nearbyfinder.Model.PlaceModel.PositionModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TomNearbyPlaceModel {
    @SerializedName("type")
    @Expose
    private String type;


    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("score")
    @Expose
    private Double score;


    @SerializedName("dist")
    @Expose
    private Double dist;


    @SerializedName("info")
    @Expose
    private String info;




    @SerializedName("poi")
    @Expose
    private PoiModel poi;


    @SerializedName("address")
    @Expose
    private AddressModel address;


    @SerializedName("position")
    @Expose
    private PositionModel position;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getDist() {
        return dist;
    }

    public void setDist(Double dist) {
        this.dist = dist;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public PoiModel getPoi() {
        return poi;
    }

    public void setPoi(PoiModel poi) {
        this.poi = poi;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public PositionModel getPosition() {
        return position;
    }

    public void setPosition(PositionModel position) {
        this.position = position;
    }
}
