package com.mas.quranwords.repository

import com.mas.quranwords.models.WordItem
import com.mas.quranwords.network.WordApiService

class WordRepository(
    private val apiService: WordApiService
) {

    suspend fun getWords(): Result<List<WordItem>> {

        return try {

            Result.success(
                apiService.getWords().words
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}