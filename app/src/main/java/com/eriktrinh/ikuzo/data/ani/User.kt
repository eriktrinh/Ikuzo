package com.eriktrinh.ikuzo.data.ani

import com.google.gson.annotations.SerializedName

data class User(val id: Int,
                @SerializedName("display_name") val displayName: String,
                @SerializedName("image_url_lge") val imageUrl: String,
                @SerializedName("image_url_med") val imageUrlSmall: String
        // Full model extras:
)