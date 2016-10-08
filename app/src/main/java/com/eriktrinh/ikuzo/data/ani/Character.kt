package com.eriktrinh.ikuzo.data.ani

import com.google.gson.annotations.SerializedName

data class Character(val id: Int,
                     @SerializedName("name_first") val firstName: String,
                     @SerializedName("name_last") val lastName: String,
                     @SerializedName("image_url_lge") val imageUrl: String,
                     val role: String?,
        // Full model extras:
                     val info: String?,
                     @SerializedName("name_japanese") val nameJapanese: String?,
                     val favourite: Boolean?,
        // Page model extras:
                     val anime: List<Anime>?
)