package com.eriktrinh.ikuzo.web

import android.content.Context
import com.eriktrinh.ikuzo.data.ani.Tokens
import com.eriktrinh.ikuzo.utils.shared_pref.AuthUtils
import com.eriktrinh.ikuzo.web.service.AuthService
import io.reactivex.Observable
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = AuthUtils.getRefreshToken(context)
        if (refreshToken != null) {
            val refreshObservable: Observable<Tokens> = ServiceGenerator.createService(AuthService::class.java).accessTokenByRefresh(refreshToken)
            try {
                val token = refreshObservable.blockingFirst()
                val accessToken = "Bearer ${token.accessToken?.trim()}"
                AuthUtils.setAccessToken(context, accessToken)

                return response.request().newBuilder()
                        .header("Authorization", accessToken)
                        .build()
            } catch (e: NoSuchElementException) {
            }
        }
        AuthUtils.setRefreshToken(context, null)
        return null
    }
}