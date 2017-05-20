package com.eriktrinh.ikuzo.data.ani

import com.google.gson.annotations.SerializedName
import java.util.*

data class Airing(val time: Date,
                  val countdown: Int,
                  @SerializedName("next_episode") val nextEpisode: Int) {
    fun formatCountdown(): String {
        val minutes = (countdown / 60) % 60
        val hours = (countdown / (60 * 60)) % 24
        val days = (countdown / (60 * 60)) / 24
        return "Ep $nextEpisode in" +
                (if (days != 0) " ${days}d" else "") +
                (if (hours != 0) " ${hours}h" else "") +
                " ${minutes}m"
    }
}