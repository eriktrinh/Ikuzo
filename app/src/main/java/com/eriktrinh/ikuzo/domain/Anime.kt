package com.eriktrinh.ikuzo.domain

import com.google.gson.annotations.SerializedName

data class Anime(val id: Int,
                 @SerializedName("title_romanji") val titleRomanji: String,
                 @SerializedName("title_english") val titleEnglish: String,
                 @SerializedName("title_japanese") val titleJapanese: String,
                 val type: String,
                 @SerializedName("start_date_fuzzy") val startDate: Int,
                 @SerializedName("end_date_fuzzy") val endDate: Int,
                 val season: Int,
                 @SerializedName("series_type") val seriesType: String,
                 val genres: List<String>,
                 val adult: Boolean,
                 @SerializedName("average_score") val averageScore: Float,
                 val popularity: Int,
                 @SerializedName("updated_at") val updatedAt: Int,
                 @SerializedName("image_url_lge") val imageUrl: String,
                 @SerializedName("total_episodes") val totalEpisodes: Int,
                 @SerializedName("airing_status") val airingStatus: String,
                 val tags: List<Tag>
) : Series {

}