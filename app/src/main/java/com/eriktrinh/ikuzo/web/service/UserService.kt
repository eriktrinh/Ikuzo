package com.eriktrinh.ikuzo.web.service

import com.eriktrinh.ikuzo.data.ani.User
import io.reactivex.Observable
import retrofit2.http.GET

interface UserService {
    @GET("user")
    fun getMe(): Observable<User>
}