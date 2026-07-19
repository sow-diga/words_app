package com.mas.quranwords.domain.extensions

import com.mas.quranwords.models.WordItem

fun WordItem.toDisplayText(): String {
    return word?.ifBlank { ayahNumber.toString() } ?: ayahNumber.toString()
}