package com.mas.quranwords.qari

data class Reciter(
    val name: String,
    val folder: String?
) {
    override fun toString(): String = name
}