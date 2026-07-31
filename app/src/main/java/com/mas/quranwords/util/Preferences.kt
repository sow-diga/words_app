package com.mas.quranwords.util

import android.content.Context
import com.mas.quranwords.models.PlayerMode

object Preferences {

    private const val PREFS_NAME = "settings"
    private const val KEY_LEARNING_MODE = "learning_mode"
    private const val KEY_PLAYER_MODE = "player_mode"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLearningMode(context: Context, mode: String) {
        prefs(context).edit().putString(KEY_LEARNING_MODE, mode).apply()
    }

    fun getLearningMode(context: Context, default: String): String {
        return prefs(context).getString(KEY_LEARNING_MODE, default) ?: default
    }

    // Player Mode
    fun savePlayerMode(context: Context, mode: PlayerMode) {
        prefs(context).edit().putString(KEY_PLAYER_MODE, mode.name).apply()
    }

    fun getPlayerMode(context: Context): PlayerMode {
        val value = prefs(context).getString(KEY_PLAYER_MODE, PlayerMode.PRACTICE.name)
        return PlayerMode.valueOf(value!!)
    }
}