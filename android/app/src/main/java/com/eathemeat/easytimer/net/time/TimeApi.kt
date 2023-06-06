package com.eathemeat.easytimer.net.time

import com.eathemeat.easytimer.data.UserInfo
import com.eathemeat.easytimer.net.BaseHttpResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface TimeApi {

    @GET("/user/signin")
    fun signIn(passport: String, password: String): Call<UserInfo>


    @POST("/user/signup")
    fun signUp(passport: String, password: String): Call<BaseHttpResult>


}