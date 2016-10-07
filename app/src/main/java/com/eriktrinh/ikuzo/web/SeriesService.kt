package com.eriktrinh.ikuzo.web

import com.eriktrinh.ikuzo.ani.domain.Anime
import com.eriktrinh.ikuzo.ani.domain.Favourite
import com.eriktrinh.ikuzo.ext.getSeason
import com.eriktrinh.ikuzo.ext.getYear
import com.eriktrinh.ikuzo.payload.EditList
import com.eriktrinh.ikuzo.payload.Id
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface SeriesService {
    @GET("browse/anime?airing_data=true")
    fun browseAnime(@Query("year") year: Int? = Calendar.getInstance().getYear(),
                    @Query("season") season: String? = Calendar.getInstance().getSeason().string,
                    @Query("status") status: String? = null,
                    @Query("type") type: String? = null,
                    @Query("sort") sort: String = "popularity-desc",
                    @Query("page") page: Int = 1
    ): Call<List<Anime>>

    @GET("anime/{id}/page")
    fun getAnimePage(@Path("id") id: Int): Call<Anime>

    @POST("anime/favourite")
    fun favAnime(@Body id: Id): Call<Favourite>
}