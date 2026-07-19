package com.mas.quranwords.repository

import com.mas.quranwords.models.WordItem
import com.mas.quranwords.network.WordApiService

class WordRepository(
    private val apiService: WordApiService
) {

    private suspend fun safeCall(call: suspend () -> List<WordItem>): Result<List<WordItem>> {
        return try {
            Result.success(call())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWords() = safeCall {
            apiService.getWords().words
        }

    suspend fun getMemorizeWords() = safeCall {
            apiService.getMemorize().words
        }

    suspend fun getMistakes() = safeCall {
            apiService.getMistakes().words
        }
    
    suspend fun getAyah() = safeCall {
            apiService.getAyah().words
        }
}