package com.mas.quranwords.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mas.quranwords.domain.model.QuranSurah

/**
 * Call anywhere QuranRepository.getVerseText(surah, ayah)
 */
object QuranRepository {

    private lateinit var surahs: List<QuranSurah>
    private val surahMap by lazy { surahs.associateBy { it.number } }

    fun init(context: Context) {
        if (::surahs.isInitialized) return

        val json = context.assets
            .open("quran.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<QuranSurah>>() {}.type

        surahs = Gson().fromJson(json, type)
    }

    /**
     * Returns the Arabic verse text.
     */
    fun getVerseText(
        surahNumber: Int,
        ayahNumber: Int
    ): String? {

        return surahMap[surahNumber]
            ?.verses
            ?.getOrNull(ayahNumber - 1)
            ?.text
    }

    /**
     * Returns the entire verse object.
     */
    fun getVerse(
        surahNumber: Int,
        ayahNumber: Int
    ) = surahMap[surahNumber]
        ?.verses
        ?.getOrNull(ayahNumber - 1)

    /**
     * Returns all verses of one surah.
     */
    fun getSurah(
        surahNumber: Int
    ) = surahMap[surahNumber]?.verses.orEmpty()

    /**
     * Returns the Arabic surah name.
     */
    fun getSurahName(
        surahNumber: Int
    ): String? {

        return surahMap[surahNumber]?.name
    }
}