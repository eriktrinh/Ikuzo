package com.eriktrinh.ikuzo.web

import com.eriktrinh.ikuzo.domain.Anime
import com.eriktrinh.ikuzo.ext.getSeason
import com.eriktrinh.ikuzo.ext.getYear
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
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
}