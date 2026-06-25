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

    suspend fun getMemorizeWords(): Result<List<WordItem>> {
        return try {
            Result.success(
                apiService.getMemorize().words
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMistakes(): Result<List<WordItem>> {
        return try {
            Result.success(
                apiService.getMistakes().words
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}