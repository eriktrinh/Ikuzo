package com.eriktrinh.ikuzo.enums

enum class Sort(val string: String,
                val display: String) {
    SCORE("score", "Score"),
    POPULARITY("popularity", "Popularity"),
    START_DATE("start_date", "Start Date"),
    END_DATE("end_date", "End Date"),
    ID("id", "Date Added")
}