package com.mas.quranwords.models

data class WordResponse(
    val version: Int,
    val message: String,
    val words: List<WordItem>
)
