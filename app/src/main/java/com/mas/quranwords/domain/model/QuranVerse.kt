package com.mas.quranwords.domain.model

import com.google.gson.annotations.SerializedName

data class QuranVerse(

    @SerializedName("id")
    val ayah: Int,

    val text: String,

    val translation: String
)