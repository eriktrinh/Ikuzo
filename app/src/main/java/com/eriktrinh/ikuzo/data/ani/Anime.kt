package com.eriktrinh.ikuzo.data.ani

import com.eriktrinh.ikuzo.data.enums.AiringStatus
import com.eriktrinh.ikuzo.utils.CalendarUtils
import com.google.gson.annotations.SerializedName

data class Anime(val id: Int,
                 @SerializedName("series_type") val seriesType: String,
                 @SerializedName("title_romaji") val titleRomaji: String,
                 @SerializedName("title_english") val titleEnglish: String,
                 @SerializedName("title_japanese") val titleJapanese: String,
                 val type: String,
                 @SerializedName("start_date_fuzzy") val startDate: Int?,
                 @SerializedName("end_date_fuzzy") val endDate: Int?,
                 val season: Int?,
                 val genres: List<String>,
                 val adult: Boolean,
                 @SerializedName("average_score") val averageScore: Float,
                 val popularity: Int,
                 @SerializedName("image_url_lge") val imageUrl: String,
                 @SerializedName("total_episodes") val totalEpisodes: Int,
                 @SerializedName("airing_status") val airingStatus: AiringStatus,
                 val tags: List<Tag>,
        // Full model extras:
                 val description: String?,
                 val favourite: Boolean?,
                 @SerializedName("score_distribution") val scoreDistribution: Map<Int, Int>?,
                 @SerializedName("list_stats") val listStats: Map<String, Int>?,
                 val duration: Int?,
                 val source: String?,
                 val airing: Airing?,
        // Page model extras:
                 val characters: List<Character>?,
                 val studio: List<Studio>?,
                 val externalLinks: List<ExternalLink>?,
                 val reviews: List<Review>?,
                 @SerializedName("youtube_id") val youtubeId: String?
) {
    fun formatLeftText(): String {
        return "$type ($totalEpisodes eps)"
    }

    fun formatCenterText(): String {
        return "$averageScore%"
    }

    fun formatRightText(): String {
        return "$popularity"
    }

    fun formatStatusText(): String {
        val dateString = if (startDate == null) "" else {
            val startYear = startDate / 10000
            val startMonth = CalendarUtils.monthToShortForm[(startDate / 100) % 100]
            " ($startMonth $startYear - ${
            if (endDate != null) "${CalendarUtils.monthToShortForm[(endDate / 100) % 100]} ${endDate / 10000}" else ""
            })"
        }
        return "${airingStatus.string.toLowerCase()}$dateString"
    }
}
