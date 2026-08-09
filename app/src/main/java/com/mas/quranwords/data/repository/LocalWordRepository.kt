package com.mas.quranwords.data.repository

import kotlinx.coroutines.flow.Flow
import com.mas.quranwords.data.db.WordRecord
import com.mas.quranwords.data.db.WordDao
import com.mas.quranwords.domain.filter.WordFilter


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

    fun getWords(filter: WordFilter): Flow<List<WordRecord>> {
        return dao.getFiltered(
            category = filter.category,
            surah = filter.surah,
            level = filter.level
        )
    }

    fun getWordCount(): Flow<Int> {
        return dao.getWordCount()
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