package com.mas.quranwords.qari

data class Reciter(
    val name: String,
    val folder: String?
) {
    val isWordOnly: Boolean
        get() = folder == null

    override fun toString(): String = name
}