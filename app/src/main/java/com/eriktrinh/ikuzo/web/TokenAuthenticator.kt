package com.eriktrinh.ikuzo.web

import android.content.Context
import com.eriktrinh.ikuzo.ani.domain.Tokens
import com.eriktrinh.ikuzo.utils.AuthUtils
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Call

class TokenAuthenticator(val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = AuthUtils.getRefreshToken(context)
        if (refreshToken != null) {
            val refreshCall: Call<Tokens> = ServiceGenerator.createService(AuthService::class.java).accessTokenByRefresh(refreshToken)
            val refreshResponse = refreshCall.execute()
            if (refreshResponse.raw().code() == 200) {
                val accessToken = "Bearer ${refreshResponse.body().accessToken?.trim()}"
                AuthUtils.setAccessToken(context, accessToken)

                return response.request().newBuilder()
                        .header("Authorization", accessToken)
                        .build()
            }
        }
        AuthUtils.setRefreshToken(context, null)
        return null
    }
}