package com.eriktrinh.ikuzo.web.service

import com.eriktrinh.ikuzo.data.ani.User
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("user")
    fun getMe(): Call<User>
}