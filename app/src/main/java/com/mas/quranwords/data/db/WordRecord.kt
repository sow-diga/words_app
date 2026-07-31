package com.mas.quranwords.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String = "WORD",
    val surahNumber: Int,
    val ayahNumber: Int,
    val word: String,
    val english: String,
    val comment: String = "",
    val wordPosition: Int,
    val level: String = "Medium",
    val lastReviewed: Long = 0,
    val reviewCount: Int = 0
)