package com.mas.quranwords.domain.filter
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordFilter(
    val category: String? = null,
    val surah: Int? = null,
    val level: String? = null
) : Parcelable