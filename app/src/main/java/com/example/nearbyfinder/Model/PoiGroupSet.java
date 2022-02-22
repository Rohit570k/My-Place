package com.example.nearbyfinder.Model;

public class PoiGroupSet {
    int id, drawableId;
    String name;
    String poiCategoriesId;

    public PoiGroupSet(int id, int drawableId, String name, String poiCategoriesId) {
        this.id = id;
        this.drawableId = drawableId;
        this.name = name;
        this.poiCategoriesId = poiCategoriesId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoiCategoriesId() {
        return poiCategoriesId;
    }

    public void setPoiCategoriesId(String poiCategoriesId) {
        this.poiCategoriesId = poiCategoriesId;
    }



}
