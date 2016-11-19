package com.eriktrinh.ikuzo.utils.ext

import com.eriktrinh.ikuzo.data.enums.Season
import java.util.*

fun Calendar.getYear(): Int {
    val season = this.getSeason()
    val month = this.get(Calendar.MONTH) + 1
    val year = this.get(Calendar.YEAR)
    return if (season == Season.WINTER && month > 6) year + 1 else year
}

fun Calendar.getSeason(): Season {
    val day = this.get(Calendar.DAY_OF_MONTH)
    val month = this.get(Calendar.MONTH) + 1
    var season: Int = (month / 3) % 4
    if (month % 3 == 0 && day < 21)
        season = if (season == 0) 3 else season - 1
    return Season.values()[season + 1]
}