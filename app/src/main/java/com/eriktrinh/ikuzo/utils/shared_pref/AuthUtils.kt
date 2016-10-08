package com.eriktrinh.ikuzo.utils.shared_pref

import android.content.Context
import android.preference.PreferenceManager
import com.eriktrinh.ikuzo.data.ani.User
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AuthUtils {
    private val KEY_ACCESS_TOKEN = "AuthUtils.KEY_ACCESS_TOKEN"
    private val KEY_REFRESH_TOKEN = "AuthUtils.KEY_REFRESH_TOKEN"

    @JvmStatic fun getAccessToken(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_ACCESS_TOKEN, null)
    }

    @JvmStatic fun getRefreshToken(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_REFRESH_TOKEN, null)
    }

    @JvmStatic fun setAccessToken(context: Context, accessToken: String?) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .commit()
    }

    @JvmStatic fun setRefreshToken(context: Context, refreshToken: String?) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .commit()
    }

    @JvmStatic fun isAuthorized(context: Context): Boolean {
        return getRefreshToken(context) != null
    }

    @JvmStatic fun clearPreferences(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .clear()
                .commit()
    }

    @JvmStatic fun setMe(context: Context, onSuccess: () -> Unit, onFailure: () -> Unit) {
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
                            MeUtils.setMyImageUrl(context, me.imageUrl)
                            onSuccess()
                        } else {
                            onFailure()
                        }
                    }

                })
    }
}