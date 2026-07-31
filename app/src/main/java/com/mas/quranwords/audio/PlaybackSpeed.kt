package com.mas.quranwords.audio

enum class PlaybackSpeed(
    val label: String,
    val value: Float
) {

    SLOW("0.50x", 0.50f),

    NORMAL_SLOW("0.75x", 0.75f),

    NORMAL("1.00x", 1.00f),

    FAST("1.25x", 1.25f),

    VERY_FAST("1.50x", 1.50f);

    override fun toString(): String = label
}