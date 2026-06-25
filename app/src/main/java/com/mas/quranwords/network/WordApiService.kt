package com.mas.quranwords.network
import com.mas.quranwords.models.WordResponse
import retrofit2.http.GET

interface WordApiService {
    @GET("words/difficult.json")
    suspend fun getWords(): WordResponse

    @GET("words/memorize.json")
    suspend fun getMemorize(): WordResponse

    @GET("words/mistake.json")
    suspend fun getMistakes(): WordResponse
}