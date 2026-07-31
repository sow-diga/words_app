package com.mas.quranwords.data.repository

import kotlinx.coroutines.flow.Flow
import com.mas.quranwords.data.db.WordRecord
import com.mas.quranwords.data.db.WordDao


class LocalWordRepository(
    private val dao: WordDao
) {

    fun getAllWords(): Flow<List<WordRecord>> {
        return dao.getAll()
    }

    fun getWordsByCategory(category: String): Flow<List<WordRecord>> {
        return dao.getByCategory(category)
    }

    fun getWordsBySurah(category: String, surah: Int): Flow<List<WordRecord>> {
        return dao.getBySurah(category, surah)
    }

    suspend fun getWord(id: Long): WordRecord? {
        return dao.get(id)
    }

    suspend fun insert(word: WordRecord) {
        dao.insert(word)
    }

    suspend fun update(word: WordRecord) {
        dao.update(word)
    }

    suspend fun delete(word: WordRecord) {
        dao.delete(word)
    }

}