package com.eriktrinh.ikuzo.domain

import com.google.gson.annotations.SerializedName

data class User(val id: Int,
                @SerializedName("display_name") val displayName: String) {
}