package com.eriktrinh.ikuzo.oauth

import android.content.Context
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthCallbacks(val context: Context, val callbacks: Callbacks) : Callback<Tokens> {

    interface Callbacks {
        fun onAuthenticated()
        fun onAuthenticationFailure()
    }
    private val TAG = "AuthCallbacks"

    override fun onResponse(call: Call<Tokens>, response: Response<Tokens>?) {
        val tokens: Tokens? = response?.body()
        if (isValidTokenResponse(tokens)) {
            AuthUtils.setAccessToken(context, "Bearer ${tokens?.accessToken}")
            Log.i(TAG, "Got access_token: ${tokens?.accessToken}")

            if (tokens?.refreshToken != null) {
                AuthUtils.setRefreshToken(context, tokens?.refreshToken)
            }

            callbacks.onAuthenticated()
        } else {
            callbacks.onAuthenticationFailure()
        }
    }

    override fun onFailure(call: Call<Tokens>, t: Throwable) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun isValidTokenResponse(tokens: Tokens?): Boolean {
        return tokens != null &&
                tokens.accessToken != null && !tokens.accessToken.isBlank()
    }
}