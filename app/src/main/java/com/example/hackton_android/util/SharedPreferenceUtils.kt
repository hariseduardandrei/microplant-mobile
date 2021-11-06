package com.example.hackton_android.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceUtils {

    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
    }

    fun getString(key: String): String {
        return sharedPreferences?.getString(key, "").orEmpty()
    }

    fun save(key: String, data: String) {
        with(sharedPreferences?.edit()) {
            this?.putString(key, data)
            this?.apply()
        }
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences?.getBoolean(key, false) ?: false
    }

    fun save(key: String, data: Boolean) {
        with(sharedPreferences?.edit()) {
            this?.putBoolean(key, data)
            this?.apply()
        }
    }

    fun getInt(key: String): Int? {
        return sharedPreferences?.getInt(key, -1)
    }

    fun save(key: String, data: Int?) {
        if (data != null) {
            with(sharedPreferences?.edit()) {
                this?.putInt(key, data)
                this?.apply()
            }
        }
    }

}