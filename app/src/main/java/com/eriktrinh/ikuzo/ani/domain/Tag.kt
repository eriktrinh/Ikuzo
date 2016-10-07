package com.eriktrinh.ikuzo.ani.domain

import com.google.gson.annotations.SerializedName

data class Tag(val id: Int,
               val name: String,
               val description: String,
               val spoiler: Boolean,
               val adult: Boolean,
               val demographic: Boolean,
               val denied: Int,
               val category: String,
               val votes: Int,
               @SerializedName("series_spoiler") val seriesSpoiler: Boolean
)