package com.eriktrinh.ikuzo.utils

import android.content.Context
import android.preference.PreferenceManager
import com.eriktrinh.ikuzo.domain.User
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    fun clearPreferences(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .clear()
                .commit()
    }

    fun setMe(context: Context, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val userService: UserService = ServiceGenerator.createService(UserService::class.java, context)
        userService.getMe()
                .enqueue(object : Callback<User> {
                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                        onFailure()
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>?) {
                        val me: User? = response?.body()
                        if (response?.code() == 200 && me != null) {
                            MeUtils.setMyDisplayName(context, me.displayName)
                            MeUtils.setMyId(context, me.id)
                            onSuccess()
                        } else {
                            onFailure()
                        }
                    }

                })
    }
}