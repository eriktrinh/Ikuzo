package com.eriktrinh.ikuzo

import android.content.Context
import com.eriktrinh.ikuzo.domain.Anime
import com.eriktrinh.ikuzo.domain.Series
import java.util.*

class SeriesLab private constructor(private val context: Context) : HashMap<Int, Series>() {
    companion object {
        private var seriesLab: SeriesLab? = null

        fun get(context: Context): SeriesLab {
            if (seriesLab == null) {
                seriesLab = SeriesLab(context)
            }
            return seriesLab!!
        }
    }

    fun put(series: Series) {
        this.put((series as Anime).id, series)
    }
}