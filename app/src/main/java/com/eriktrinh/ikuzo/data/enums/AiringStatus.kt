package com.eriktrinh.ikuzo.data.enums

import com.google.gson.annotations.SerializedName

enum class AiringStatus(val string: String) {
    NONE(""),
    @SerializedName("not yet aired") NOT_YET("Not Yet Aired"),
    @SerializedName("currently airing") CURRENTLY("Currently Airing"),
    @SerializedName("finished airing") FINISHED("Finished Airing"),
    @SerializedName("cancelled") CANCELLED("Cancelled")
}