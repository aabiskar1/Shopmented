package com.aabiskar.shopmented;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("save.php")
    Call<Users> saveNote(
      @Field("name") String name,
      @Field("email") String email,
      @Field("password") String password,
      @Field("phone") String phone
    );


}
