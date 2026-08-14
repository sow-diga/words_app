package com.mas.quranwords.data.settings

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mas.quranwords.qari.Reciters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(
    name = "quran_words_settings"
)

class SettingsRepository(
    private val context: Context
) {

    private object Keys {
        val MAX_LISTEN = intPreferencesKey("max_listen")
        val MAX_REPEAT = intPreferencesKey("max_repeat")
        val NUMBERS_SESSION_SIZE = intPreferencesKey("numbers_session_size")

        val PRACTICE_RECITER_1 = stringPreferencesKey("practice_reciter_1")
        val PRACTICE_RECITER_2 = stringPreferencesKey("practice_reciter_2")
    }

    val maxListen: Flow<Int> =
        context.settingsDataStore.data.map { preferences ->
            preferences[Keys.MAX_LISTEN] ?: 5
        }

    val maxRepeat: Flow<Int> =
        context.settingsDataStore.data.map { preferences ->
            preferences[Keys.MAX_REPEAT] ?: 10
        }

    val numbersSessionSize: Flow<Int> =
        context.settingsDataStore.data.map { preferences ->
            preferences[Keys.NUMBERS_SESSION_SIZE] ?: 20
        }

    val practiceReciter1: Flow<String> =
        context.settingsDataStore.data.map { preferences ->
            preferences[Keys.PRACTICE_RECITER_1]
                ?: Reciters.AYMAN_SUWAID.folder!!
        }

    val practiceReciter2: Flow<String> =
        context.settingsDataStore.data.map { preferences ->
            preferences[Keys.PRACTICE_RECITER_2]
                ?: Reciters.HUSARY.folder!!
        }

    suspend fun setMaxListen(value: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[Keys.MAX_LISTEN] = value
        }
    }

    suspend fun setMaxRepeat(value: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[Keys.MAX_REPEAT] = value
        }
    }

    suspend fun setNumbersSessionSize(value: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[Keys.NUMBERS_SESSION_SIZE] = value
        }
    }

    suspend fun setPracticeReciter1(folder: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[Keys.PRACTICE_RECITER_1] = folder
        }
    }

    suspend fun setPracticeReciter2(folder: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[Keys.PRACTICE_RECITER_2] = folder
        }
    }
}