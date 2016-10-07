package com.eriktrinh.ikuzo.ani.domain

import com.eriktrinh.ikuzo.enums.ListStatus
import com.eriktrinh.ikuzo.payload.Id
import com.google.gson.annotations.SerializedName

data class Record(@SerializedName("list_status") var listStatus: ListStatus,
                  var score: Int,
                  @SerializedName("episodes_watched") var episodesWatched: Int,
                  val anime: Id
)