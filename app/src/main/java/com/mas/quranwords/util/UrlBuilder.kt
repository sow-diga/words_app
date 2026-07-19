package com.mas.quranwords.util

import com.mas.quranwords.models.WordItem
import com.mas.quranwords.qari.Reciter

object UrlBuilder {

    fun buildAudio(reciter: Reciter, wordItem: WordItem): String {
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

    /** val urls =UrlBuilder.buildAudio(selectedReciter, wordItem, 3)
     * AudioPlayer.play(urls)
     * */
    fun buildAudio(reciter: Reciter, wordItem: WordItem, count: Int): List<String> {
        val surah = wordItem.surahNumber.toString().padStart(3, '0')
        val startAyah = wordItem.ayahNumber ?: throw IllegalArgumentException("Ayah number is null")
        // DEFAULT (WBW or fallback single audio)
        if (reciter.folder == null) {
            val verse = startAyah.toString().padStart(3, '0')
            return listOf(
                "https://audio.qurancdn.com/wbw/${surah}_${verse}_${
                    (wordItem.wordPosition ?: 1).toString().padStart(3, '0')
                }.mp3"
            )
        }

        return (startAyah until startAyah + count).map { ayah ->
            val verse = ayah.toString().padStart(3, '0')
            "https://everyayah.com/data/${reciter.folder}/$surah$verse.mp3"
        }
    }

    fun buildImage(
        wordItem: WordItem
    ): String {
        return "https://everyayah.com/data/images_png/${wordItem.surahNumber}_${wordItem.ayahNumber}.png"
    }
}