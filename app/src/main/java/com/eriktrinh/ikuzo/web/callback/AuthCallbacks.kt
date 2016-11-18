package com.eriktrinh.ikuzo.web.callback

import android.content.Context
import android.util.Log
import com.eriktrinh.ikuzo.data.ani.Tokens
import com.eriktrinh.ikuzo.utils.shared_pref.AuthUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthCallbacks(val context: Context, val delegate: Delegate) : Callback<Tokens> {

    interface Delegate {
        fun onAuthenticated()
        fun onAuthenticationFailure()
    }

    private val TAG = "AuthCallbacks"

    override fun onResponse(call: Call<Tokens>, response: Response<Tokens>?) {
        val tokens: Tokens? = response?.body()
        if (isValidTokenResponse(tokens)) {
            AuthUtils.setAccessToken(context, "Bearer ${tokens?.accessToken?.trim()}")
            Log.i(TAG, "Got access_token: ${tokens?.accessToken}")

            if (tokens?.refreshToken != null) {
                AuthUtils.setRefreshToken(context, tokens?.refreshToken.trim())
            }
            delegate.onAuthenticated()
        } else {
            delegate.onAuthenticationFailure()
        }
    }

    override fun onFailure(call: Call<Tokens>, t: Throwable) {
        delegate.onAuthenticationFailure()
    }


    private fun isValidTokenResponse(tokens: Tokens?): Boolean {
        return tokens != null &&
                tokens.accessToken != null && !tokens.accessToken.isBlank()
    }
}