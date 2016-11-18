package com.eriktrinh.ikuzo.data.ani

import com.google.gson.annotations.SerializedName

data class Studio(val id: Int,
                  @SerializedName("studio_name") val name: String,
                  @SerializedName("studio_wiki") val wikiUrl: String,
                  val favourite: Boolean,
        // Page model extras:
                  val anime: List<Anime>?
)