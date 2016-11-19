package com.eriktrinh.ikuzo.data.enums

import com.google.gson.annotations.SerializedName

enum class Type(val string: String) {
    NONE(""),
    TV("TV"),
    @SerializedName("Movie") MOVIE("Movie"),
    @SerializedName("Special") SPECIAL("Special"),
    OVA("OVA"),
    ONA("ONA"),
    @SerializedName("TV Short") TV_SHORT("TV Short")
}
