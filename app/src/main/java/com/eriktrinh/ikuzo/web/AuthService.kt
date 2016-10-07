package com.eriktrinh.ikuzo.web

import com.eriktrinh.ikuzo.ani.domain.Tokens
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    companion object {
        const val CLIENT_ID = "burning47-3s3qj"
        private const val CLIENT_SECRET = "57pETV0LMBQApEDJL6oIGWkR338QC"
        const val REDIRECT_URI = "com.eriktrinh.ikuzo"
    }

    @POST("auth/access_token?" +
            "grant_type=authorization_code&" +
            "client_id=$CLIENT_ID&" +
            "client_secret=$CLIENT_SECRET&" +
            "redirect_uri=$REDIRECT_URI")
    fun accessTokenByCode(@Query("code") code: String): Call<Tokens>

    @POST("auth/access_token?" +
            "grant_type=refresh_token&" +
            "client_id=$CLIENT_ID&" +
            "client_secret=$CLIENT_SECRET")
    fun accessTokenByRefresh(@Query("refresh_token") refreshToken: String): Call<Tokens>
}