package com.example.newloginretro.services;


import com.example.newloginretro.model.User;
import com.example.newloginretro.model.UserTenent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceApi {
    @GET("register-ten.php")
    Call<User> doRegistration(@Query("name") String name, @Query("email") String email, @Query("phone") String phone, @Query("password") String password);



    @GET("login-tenent.php")
    Call<UserTenent> doLogin(@Query("email") String email, @Query("password") String password);


}
