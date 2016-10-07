package com.eriktrinh.ikuzo.web

import com.eriktrinh.ikuzo.ani.domain.SeriesList
import com.eriktrinh.ikuzo.payload.EditList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ListService {
    @GET("user/{id}/animelist")
    fun getList(@Path("id") id: Int): Call<SeriesList>

    @DELETE("animelist/{id}")
    fun delListEntry(@Path("id") id: Int): Call<ResponseBody>

    @PUT("animelist")
    fun putListEntry(@Body entry: EditList): Call<ResponseBody>
}