package com.eriktrinh.ikuzo.ani.domain

import com.google.gson.annotations.SerializedName

data class User(val id: Int,
                @SerializedName("display_name") val displayName: String,
                @SerializedName("image_url_lge") val imageUrl: String
        // Full model extras:
)