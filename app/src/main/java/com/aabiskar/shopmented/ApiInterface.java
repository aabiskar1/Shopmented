package com.aabiskar.shopmented;

import com.aabiskar.shopmented.models.APIResponse;
import com.aabiskar.shopmented.models.Banners;
import com.aabiskar.shopmented.models.Cart;
import com.aabiskar.shopmented.models.CartAmount;
import com.aabiskar.shopmented.models.CartInsert;
import com.aabiskar.shopmented.models.OrderResponse;
import com.aabiskar.shopmented.models.Products;
import com.aabiskar.shopmented.models.Users;
import com.aabiskar.shopmented.models.VBucks;

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
    @POST("cart/getCartDetails.php")
    Call<ArrayList<Cart>> getCartList(
            @Field("customer_id") int customer_id
    );


    @FormUrlEncoded
    @POST("cart/insertIntoCart.php")
    Call<CartInsert> insertCart(
            @Field("product_id") int product_id,
            @Field("customer_id") int customer_id,
            @Field("quantity") int quantity
    );

    @FormUrlEncoded
    @POST("cart/deleteFromCart.php")
    Call<CartInsert> deleteFromCart(
            @Field("product_id") int product_id,
            @Field("customer_id") int customer_id
    );

    @FormUrlEncoded
    @POST("cart/updateCartQty.php")
    Call<CartInsert> updateCartQty(
            @Field("product_id") int product_id,
            @Field("customer_id") int customer_id,
            @Field("quantity") int quantity

    );

    @FormUrlEncoded
    @POST("cart/getCartTotalAmount.php")
    Call<ArrayList<CartAmount>> getTotalCartAmount(
            @Field("customer_id") int customer_id
    );


    @FormUrlEncoded
    @POST("payment/getUserVBucks.php")
    Call<ArrayList<VBucks>> getUserVBucks(
            @Field("customer_id") int customer_id
    );

    @FormUrlEncoded
    @POST("payment/loadIntoVBucks.php")
    Call<CartInsert> loadVBucks(
            @Field("customer_id") int customer_id,
            @Field("amount") Double amount
    );

    @FormUrlEncoded
    @POST("order/createOrder.php")
    Call<OrderResponse> createOrder(
            @Field("customer_id") int customer_id,
            @Field("role_id") int role_id,
            @Field("transaction_total_amt") Double transaction_total_amt,
            @Field("shipping_address") String shipping_address,
            @Field("status_id") String status_id,
            @Field("order_id") String order_id,
            @Field("transaction_id") String transaction_id,
            @Field("transaction_mode") String transaction_mode,
            @Field("transaction_date") String transaction_date,
            @Field("transaction_status") String transaction_status
    );




}
