package com.aabiskar.shopmented.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banners {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
