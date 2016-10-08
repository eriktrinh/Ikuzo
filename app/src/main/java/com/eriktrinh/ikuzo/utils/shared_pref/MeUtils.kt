package com.eriktrinh.ikuzo.utils.shared_pref

import android.content.Context
import android.preference.PreferenceManager

object MeUtils {
    private val KEY_USER_ID = "MeUtils.KEY_USER_ID"
    private val KEY_DISPLAY_NAME = "MeUtils.KEY_DISPLAY_NAME"
    private val KEY_USER_IMAGE = "MeUtils.KEY_USER_IMAGE"

    @JvmStatic fun getMyId(context: Context): Int? {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(KEY_USER_ID, -1)
    }

    @JvmStatic fun setMyId(context: Context, id: Int?) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(KEY_USER_ID, id ?: -1)
                .commit()
    }

    @JvmStatic fun getMyDisplayName(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_DISPLAY_NAME, null)
    }

    @JvmStatic fun setMyDisplayName(context: Context, name: String?) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_DISPLAY_NAME, name)
                .commit()
    }

    @JvmStatic fun getMyImageUrl(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_USER_IMAGE, null)
    }

    @JvmStatic fun setMyImageUrl(context: Context, name: String?) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_USER_IMAGE, name)
                .commit()
    }
}