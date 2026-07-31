package com.mas.quranwords

import android.app.Application
import com.mas.quranwords.data.QuranRepository
import com.mas.quranwords.player.AudioPlayer
import com.mas.quranwords.data.db.AppDatabase

class MasApp : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        QuranRepository.init(this)
        AudioPlayer.initialize(this)
        database = AppDatabase.getInstance(this)
    }
}