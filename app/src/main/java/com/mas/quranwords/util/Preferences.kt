package com.mas.quranwords.util

import android.content.Context

object Preferences {

    private const val PREFS_NAME = "settings"
    private const val KEY_LEARNING_MODE = "learning_mode"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLearningMode(context: Context, mode: String) {
        prefs(context).edit().putString(KEY_LEARNING_MODE, mode).apply()
    }

    fun getLearningMode(context: Context, default: String): String {
        return prefs(context).getString(KEY_LEARNING_MODE, default) ?: default
    }
}