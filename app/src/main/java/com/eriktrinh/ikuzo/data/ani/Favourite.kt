package com.eriktrinh.ikuzo.data.ani

import com.google.gson.annotations.SerializedName

data class Favourite(val id: Int,
                     @SerializedName("favourite_type") val favouriteType: Int,
                     @SerializedName("user_id") val userId: Int,
                     @SerializedName("favourite_id") val favouriteId: Int,
                     val order: Int?
)