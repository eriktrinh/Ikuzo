package com.eriktrinh.ikuzo

import com.eriktrinh.ikuzo.domain.Anime
import java.util.*

object SeriesLab : HashMap<Int, Anime>() {
    fun put(anime: Anime) {
        this.put(anime.id, anime)
    }
}