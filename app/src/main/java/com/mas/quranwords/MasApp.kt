package com.mas.quranwords

import android.app.Application
import com.mas.quranwords.data.QuranRepository

class MasApp : Application() {

    override fun onCreate() {
        super.onCreate()
        QuranRepository.init(this)
    }
}