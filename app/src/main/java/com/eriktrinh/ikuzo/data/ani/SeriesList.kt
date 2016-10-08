package com.eriktrinh.ikuzo.data.ani

import com.google.gson.annotations.SerializedName

data class SeriesList(val id: Int,
                      @SerializedName("display_name") val displayName: String,
                      val lists: Lists
)

data class Lists(val watching: List<Record>?,
                 val completed: List<Record>?,
                 val dropped: List<Record>?,
                 @SerializedName("plan_to_watch") val planToWatch: List<Record>?,
                 @SerializedName("on_hold") val onHold: List<Record>
) {
    fun getRecordById(id: Int): Record? {
        val lists = listOf(watching, completed, dropped, planToWatch, onHold)
        lists.forEach {
            val find = it?.find { it.anime.id == id }
            if (find != null)
                return find
        }
        return null
    }
}