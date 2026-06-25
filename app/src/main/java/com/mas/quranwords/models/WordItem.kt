package com.mas.quranwords.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class WordItem(
    val surah: String?,
    val surahNumber: Int?,
    val ayahNumber: Int?,
    val word: String?,
    val wordPosition: Int?,
    val audio: String?,
    val image: String?,
    val husary: String?,
    val suwaid: String?,
    val level: String?
) : Parcelable
