package com.mas.quranwords.models

import java.io.Serializable

data class WordItem(
    val surah: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val word: String,
    val wordPosition: Int,
    val audio: String,
    val image: String,
    val husary: String,
    val suwaid: String,
    val level: String
) : Serializable
