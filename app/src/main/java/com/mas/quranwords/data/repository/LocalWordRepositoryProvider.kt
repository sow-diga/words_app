package com.mas.quranwords.data.repository

import android.content.Context
import com.mas.quranwords.data.db.AppDatabase


object LocalWordRepositoryProvider {


    fun get(
        context: Context
    ): LocalWordRepository {


        val dao =
            AppDatabase
                .getInstance(context)
                .wordDao()


        return LocalWordRepository(
            dao
        )
    }
}