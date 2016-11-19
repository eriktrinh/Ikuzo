package com.eriktrinh.ikuzo.web.service

import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.data.ani.Favourite
import com.eriktrinh.ikuzo.data.payload.Id
import retrofit2.Call
import retrofit2.http.*

interface SeriesService {
    @GET("browse/anime?airing_data=true")
    fun browseAnime(@Query("year") year: Int?,
                    @Query("season") season: String?,
                    @Query("status") status: String?,
                    @Query("type") type: String?,
                    @Query("sort") sort: String,
                    @Query("page") page: Int = 1
    ): Call<List<Anime>>

    @GET("anime/{id}/page")
    fun getAnimePage(@Path("id") id: Int): Call<Anime>

    @POST("anime/favourite")
    fun favAnime(@Body id: Id): Call<Favourite>
}