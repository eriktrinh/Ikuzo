package com.eriktrinh.ikuzo.enums

import com.google.gson.annotations.SerializedName

enum class ListStatus(val string: String) {
    NONE(""),
    @SerializedName("watching") WATCHING("watching"),
    @SerializedName("plan to watch") PLAN_TO_WATCH("plan to watch"),
    @SerializedName("completed") COMPLETED("completed"),
    @SerializedName("on-hold") ON_HOLD("on hold"),
    @SerializedName("dropped") DROPPED("dropped")
}