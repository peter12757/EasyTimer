package com.eathemeat.easytimer.net.user

import com.eathemeat.easytimer.net.BaseHttpResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * author:PeterX
 * time:2023/8/2 0002
 */

interface UserAPI {

    @POST("/user/register")
    suspend fun register(passport: String, password: String,phone:String,nickName:String): Call<User>

    @GET("/user/signin")
    fun signIn(passport: String, password: String,phone:String): Call<User>
}