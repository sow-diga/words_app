package com.mas.quranwords.models

data class WordForm(
    val word: String = "",
    val english: String = "",
    val surahNumber: Int? = null,
    val ayahNumber: Int? = null,
    val wordPosition: Int? = null,
    val category: String = "WORD",
    val level: String = "Medium",
    val comment: String = ""
)