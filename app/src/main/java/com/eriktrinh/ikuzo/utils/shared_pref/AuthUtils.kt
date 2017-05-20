package com.eriktrinh.ikuzo.utils.shared_pref

import android.content.Context
import android.preference.PreferenceManager
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.service.UserService
import io.reactivex.android.schedulers.AndroidSchedulers

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
                .apply()
    }

    @JvmStatic fun setRefreshToken(context: Context, refreshToken: String?) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .apply()
    }

    @JvmStatic fun isAuthorized(context: Context): Boolean {
        return getRefreshToken(context) != null
    }

    @JvmStatic fun clearPreferences(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .clear()
                .apply()
    }

    @JvmStatic fun setMe(context: Context, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val userService: UserService = ServiceGenerator.createService(UserService::class.java, context)
        userService.getMe()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { (id, displayName, imageUrl) ->
                            MeUtils.setMyDisplayName(context, displayName)
                            MeUtils.setMyId(context, id)
                            MeUtils.setMyImageUrl(context, imageUrl)
                            onSuccess()
                        }, { _ -> onFailure() }
                )
    }
}