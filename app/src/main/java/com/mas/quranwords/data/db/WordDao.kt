package com.mas.quranwords.data.db

import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
interface WordDao {

    @Query("SELECT * FROM words ORDER BY surahNumber, ayahNumber")
    fun getAll(): Flow<List<WordRecord>>

    @Query("SELECT * FROM words WHERE category = :category ORDER BY surahNumber, ayahNumber")
    fun getByCategory(category: String): Flow<List<WordRecord>>

    @Query("SELECT * FROM words WHERE category = :category AND surahNumber = :surah ORDER BY ayahNumber")
    fun getBySurah(category: String, surah: Int): Flow<List<WordRecord>>

    @Query("SELECT * FROM words WHERE id = :id")
    suspend fun get(id: Long): WordRecord?

    @Insert
    suspend fun insert(word: WordRecord)

    @Update
    suspend fun update(word: WordRecord)

    @Delete
    suspend fun delete(word: WordRecord)

    @Query("DELETE FROM words")
    suspend fun deleteAll()
}