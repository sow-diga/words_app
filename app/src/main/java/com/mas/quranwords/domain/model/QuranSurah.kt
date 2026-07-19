package com.mas.quranwords.domain.model

import com.google.gson.annotations.SerializedName

data class QuranSurah(

    @SerializedName("id")
    val number: Int,

    val name: String,

    val transliteration: String,

    val translation: String,

    val type: String,

    @SerializedName("total_verses")
    val totalVerses: Int,

    val verses: List<QuranVerse>
)