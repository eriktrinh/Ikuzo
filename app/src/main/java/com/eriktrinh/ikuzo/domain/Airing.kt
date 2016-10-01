package com.eriktrinh.ikuzo.domain

import com.google.gson.annotations.SerializedName
import java.util.*

data class Airing(val time: Date,
                  val countdown: Int,
                  @SerializedName("next_episode") val nextEpisode: Int)