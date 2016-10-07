package com.eriktrinh.ikuzo.payload

import com.eriktrinh.ikuzo.enums.ListStatus
import com.google.gson.annotations.SerializedName

data class EditList(val id: Int,
                    val score: Int?,
                    @SerializedName("episodes_watched") val episodesWatched: Int?,
                    @SerializedName("list_status") val listStatus: ListStatus?
)