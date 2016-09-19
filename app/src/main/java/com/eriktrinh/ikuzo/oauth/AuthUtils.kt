package com.eriktrinh.ikuzo.oauth

import android.content.Context
import android.preference.PreferenceManager

object AuthUtils {
    private val KEY_ACCESS_TOKEN = "AuthUtils.KEY_ACCESS_TOKEN"
    private val KEY_REFRESH_TOKEN = "AuthUtils.KEY_REFRESH_TOKEN"

    fun getAccessToken(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_ACCESS_TOKEN, null)
    }

    fun getRefreshToken(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_REFRESH_TOKEN, null)
    }

    fun setAccessToken(context: Context, accessToken: String?) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .commit()
    }

    fun setRefreshToken(context: Context, refreshToken: String?) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .commit()
    }

    fun isAuthorized(context: Context): Boolean {
        return getRefreshToken(context) != null
    }
}