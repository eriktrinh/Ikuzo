package com.eriktrinh.ikuzo

import android.content.Context
import com.eriktrinh.ikuzo.domain.Anime
import java.util.*

class SeriesLab private constructor(private val context: Context) : HashMap<Int, Anime>() {
    companion object {
        private var seriesLab: SeriesLab? = null

        fun get(context: Context): SeriesLab {
            if (seriesLab == null) {
                seriesLab = SeriesLab(context)
            }
            return seriesLab!!
        }
    }

    fun put(anime: Anime) {
        this.put(anime.id, anime)
    }
}