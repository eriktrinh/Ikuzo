package com.eriktrinh.ikuzo.ext

import com.eriktrinh.ikuzo.enums.Season
import java.util.*

fun Calendar.getYear(): Int {
    return this.get(Calendar.YEAR)
}

fun Calendar.getSeason(): Season {
    val day = this.get(Calendar.DAY_OF_MONTH)
    val month = this.get(Calendar.MONTH) + 1
    var season: Int = (month / 3) % 4
    if (month % 3 == 0 && day < 21)
        season = if (season == 0) 3 else season - 1
    return Season.values()[season]
}