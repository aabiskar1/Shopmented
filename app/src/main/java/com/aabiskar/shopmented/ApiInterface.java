package com.aabiskar.shopmented;

import com.aabiskar.shopmented.models.Banners;
import com.aabiskar.shopmented.models.Products;
import com.aabiskar.shopmented.models.Users;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("register.php")
    Call<Users> saveNote(
      @Field("name") String name,
      @Field("email") String email,
      @Field("password") String password,
      @Field("phone") String phone
    );

    @GET("products.php")
    Call<ArrayList<Products>> getAllProducts();

    @GET("getHomeBanner.php")
    Call<ArrayList<Banners>> getHomeBanners();

    @FormUrlEncoded
    @POST("getProduct.php")
    Call<ArrayList<Products>> getProduct(
            @Field("model") String model
    );




}
