package com.eriktrinh.ikuzo.web

import com.eriktrinh.ikuzo.domain.Anime
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SeriesService {
    @GET("browse/anime?sort=popularity-desc&year=2016&season=Summer")
    fun browseAnime(): Call<List<Anime>>

    @GET("anime/{id}/page")
    fun getAnimePage(@Path("id") id: Int): Call<Anime>
}