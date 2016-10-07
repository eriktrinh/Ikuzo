package com.eriktrinh.ikuzo.ani.domain

import com.google.gson.annotations.SerializedName

data class Anime(val id: Int,
                 @SerializedName("series_type") val seriesType: String,
                 @SerializedName("title_romanji") val titleRomanji: String,
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
                 @SerializedName("airing_status") val airingStatus: String?,
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
)