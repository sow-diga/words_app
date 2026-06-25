package com.mas.quranwords.util

import com.mas.quranwords.models.WordItem
import com.mas.quranwords.qari.Reciter

object UrlBuilder {

    fun buildAudio(
        reciter: Reciter,
        wordItem: WordItem
    ): String {
        val sss = wordItem.surahNumber
            .toString()
            .padStart(3, '0')

        val vvv = wordItem.ayahNumber
            .toString()
            .padStart(3, '0')

        val ppp = wordItem.wordPosition.toString().padStart(3, '0')

        // Default audio
        if (reciter.folder == null) {
            return "https://audio.qurancdn.com/wbw/${sss}_${vvv}_${ppp}.mp3"
        }

        return "https://everyayah.com/data/${reciter.folder}/$sss$vvv.mp3"
    }

    fun buildImage(
        wordItem: WordItem
    ): String {
        return "https://everyayah.com/data/images_png/${wordItem.surahNumber}_${wordItem.ayahNumber}.png"
    }
}