package com.eriktrinh.ikuzo.utils

import com.eriktrinh.ikuzo.data.ani.Anime
import java.util.*

object SeriesLab : HashMap<Int, Anime>() {
    fun put(anime: Anime) {
        this.put(anime.id, anime)
    }
}