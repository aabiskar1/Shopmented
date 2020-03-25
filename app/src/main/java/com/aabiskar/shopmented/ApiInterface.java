package com.aabiskar.shopmented;

import com.aabiskar.shopmented.models.APIResponse;
import com.aabiskar.shopmented.models.Banners;
import com.aabiskar.shopmented.models.Cart;
import com.aabiskar.shopmented.models.CartInsert;
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

    @FormUrlEncoded
    @POST("cate_products.php")
    Call<ArrayList<Products>> getCateProduct(
            @Field("post_cate") String post_cate
    );



    @FormUrlEncoded
    @POST("login.php")
    Call<APIResponse> loginUser(
            @Field("email") String email,@Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<APIResponse> registerUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("getCartDetails.php")
    Call<ArrayList<Cart>> getCartList(
            @Field("customer_id") int customer_id
    );


    @FormUrlEncoded
    @POST("cart/insertIntoCart.php")
    Call<CartInsert> insertCart(
            @Field("product_id") int product_id,
            @Field("customer_id") int customer_id

    );





}
