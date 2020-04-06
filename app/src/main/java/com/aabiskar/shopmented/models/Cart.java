package com.aabiskar.shopmented.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cart {

    @SerializedName("product_id")
    @Expose
    private String productId;

    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("model_number")
    @Expose
    private String modelNumber;
    @SerializedName("ar_name")
    @Expose
    private String arName;
    @SerializedName("price")
    @Expose
    private String price;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getArName() {
        return arName;
    }

    public void setArName(String arName) {
        this.arName = arName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
