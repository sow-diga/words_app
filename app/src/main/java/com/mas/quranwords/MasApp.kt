package com.mas.quranwords

import android.app.Application
import com.mas.quranwords.data.QuranRepository
import com.mas.quranwords.player.AudioPlayer

class MasApp : Application() {

    override fun onCreate() {
        super.onCreate()
        QuranRepository.init(this)
        AudioPlayer.initialize(this)
    }
}