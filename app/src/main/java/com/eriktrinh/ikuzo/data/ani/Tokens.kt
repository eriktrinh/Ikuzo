package com.eriktrinh.ikuzo.data.ani

import com.google.gson.annotations.SerializedName

data class Tokens(@SerializedName("access_token") val accessToken: String?,
                  @SerializedName("token_type") val tokenType: String?,
                  val expires: Int?,
                  @SerializedName("expires_in") val expiresIn: Int?,
                  @SerializedName("refresh_token") val refreshToken: String?
)