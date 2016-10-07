package com.eriktrinh.ikuzo.ani.domain

import com.google.gson.annotations.SerializedName
import java.util.*

data class Review(val id: Int,
                  val summary: String,
                  val rating: Int,
                  @SerializedName("rating_amount") val ratingAmount: Int,
                  @SerializedName("user_rating") val userRating: Int, // Current user rating of review. (0 no rating, 1 up/positive rating, 2 down/negative rating)
                  val score: Int,
                  val private: Int,
                  val text: String,
                  val user: User,
                  val date: Date,
                  val anime: Anime?
)