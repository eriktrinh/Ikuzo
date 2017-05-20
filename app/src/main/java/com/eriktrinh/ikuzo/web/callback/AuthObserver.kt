package com.eriktrinh.ikuzo.web.callback

import android.content.Context
import android.util.Log
import com.eriktrinh.ikuzo.data.ani.Tokens
import com.eriktrinh.ikuzo.utils.shared_pref.AuthUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class AuthObserver(val context: Context, val delegate: Delegate) : Observer<Tokens> {

    interface Delegate {
        fun onAuthenticated()
        fun onAuthenticationFailure()
    }

    private val TAG = "AuthObserver"

    override fun onSubscribe(d: Disposable?) {
    }

    override fun onError(e: Throwable?) {
        delegate.onAuthenticationFailure()
    }

    override fun onNext(tokens: Tokens?) {
        if (isValidTokenResponse(tokens)) {
            AuthUtils.setAccessToken(context, "Bearer ${tokens?.accessToken?.trim()}")
            Log.i(TAG, "Got access_token: ${tokens?.accessToken}")

            if (tokens?.refreshToken != null) {
                AuthUtils.setRefreshToken(context, tokens.refreshToken.trim())
            }
            delegate.onAuthenticated()
        } else {
            delegate.onAuthenticationFailure()
        }
    }

    override fun onComplete() {
    }

    private fun isValidTokenResponse(tokens: Tokens?): Boolean {
        return tokens != null &&
                tokens.accessToken != null && !tokens.accessToken.isBlank()
    }
}