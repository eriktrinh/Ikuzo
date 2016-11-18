package com.eriktrinh.ikuzo.data.ani

import com.eriktrinh.ikuzo.data.enums.ListStatus
import com.eriktrinh.ikuzo.data.payload.Id
import com.google.gson.annotations.SerializedName

data class Record(@SerializedName("list_status") var listStatus: ListStatus,
                  var score: Int,
                  @SerializedName("episodes_watched") var episodesWatched: Int,
                  val anime: Id
)