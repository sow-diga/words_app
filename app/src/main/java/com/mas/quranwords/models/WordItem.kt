package com.mas.quranwords.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class WordItem(
    val category: String?,
    val surah: String?,
    val surahNumber: Int?,
    val ayahNumber: Int?,
    val word: String?,
    val wordPosition: Int?,
    val english: String,
    val comment: String = "",
    val level: String?
) : Parcelable
