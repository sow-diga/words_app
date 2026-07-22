package com.mas.quranwords.data.mapper

import com.mas.quranwords.data.db.WordRecord
import com.mas.quranwords.models.WordItem

fun WordItem.toWordRecord(
    category: String = "WORD"
): WordRecord {

    return WordRecord(
        category = category,
        surahNumber = surahNumber ?: 0,
        ayahNumber = ayahNumber ?: 0,
        word = word ?: "",
        english = english ?: "",
        comment = comment ?: "",
        wordPosition = wordPosition ?: 0,
        level = level ?: "Medium"
    )
}

fun WordRecord.toWordItem(): WordItem {
    return WordItem(
        category = category,
        surah = null,
        surahNumber = surahNumber,
        ayahNumber = ayahNumber,
        word = word,
        english = english,
        comment = comment,
        wordPosition = wordPosition,
        level = level
    )
}