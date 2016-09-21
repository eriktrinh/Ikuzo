package com.eriktrinh.ikuzo.web

import com.eriktrinh.ikuzo.domain.User
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("user")
    fun getMe(): Call<User>
}