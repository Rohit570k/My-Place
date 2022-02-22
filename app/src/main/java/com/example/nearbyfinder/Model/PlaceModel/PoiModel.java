package com.example.nearbyfinder.Model.PlaceModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PoiModel {

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("phone")
    @Expose
    private String phone;


    @SerializedName("url")
    @Expose
    private String url;


    @SerializedName("categories")
    @Expose
    private List<String> categories = null;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

}
