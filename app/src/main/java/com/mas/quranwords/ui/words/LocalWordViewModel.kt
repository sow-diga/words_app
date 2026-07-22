package com.mas.quranwords.ui.words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.quranwords.data.db.WordRecord
import com.mas.quranwords.data.repository.LocalWordRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LocalWordViewModel(
    private val repository: LocalWordRepository
) : ViewModel() {


    val words =
        repository.getAllWords()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun loadByCategory(category: String) {
        // Later we can switch this dynamically
        // without changing fragments
    }

    fun insert(word: WordRecord) {
        viewModelScope.launch {
            repository.insert(word)
        }
    }

    fun update(word: WordRecord) {
        viewModelScope.launch {
            repository.update(word)
        }
    }

    fun delete(word: WordRecord) {
        viewModelScope.launch {
            repository.delete(word)
        }
    }

    fun getWord(id: Long, onResult: (WordRecord?) -> Unit) {
        viewModelScope.launch {
            val word = repository.getWord(id)
            onResult(word)
        }
    }
}