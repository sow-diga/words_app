package com.mas.quranwords.ui.words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.quranwords.data.db.WordRecord
import com.mas.quranwords.data.repository.LocalWordRepository
import com.mas.quranwords.domain.filter.WordFilter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest

class LocalWordViewModel(
    private val repository: LocalWordRepository
) : ViewModel() {
    private var wordList: List<WordRecord> = emptyList()

    private var currentIndex = -1

    private val _currentWord = MutableStateFlow<WordRecord?>(null)
    val currentWord: StateFlow<WordRecord?> = _currentWord
    private val _filter = MutableStateFlow(WordFilter())
    val filter = _filter.asStateFlow()

    /*
    val words =
        repository.getAllWords()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
*/
    fun loadByCategory(category: String) {
        // Later we can switch this dynamically
        // without changing fragments
    }

    // TODO Upgrade project to AGP 8.x + Kotlin 2.x after the current feature work is finished.
    val words = _filter.flatMapLatest { filter ->
            repository.getWords(filter)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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

    fun loadWord(id: Long, filter: WordFilter) {
        viewModelScope.launch {
            wordList = repository.getWords(filter).first()
            currentIndex = wordList.indexOfFirst { it.id == id }
            if (currentIndex != -1) {
                _currentWord.value = wordList[currentIndex]
            }
        }
    }

    fun nextWord() {
        if (currentIndex < wordList.lastIndex) {
            currentIndex++
            _currentWord.value = wordList[currentIndex]
        }
    }

    fun previousWord() {
        if (currentIndex > 0) {
            currentIndex--
            _currentWord.value = wordList[currentIndex]
        }
    }

    val hasNext: Boolean
        get() = currentIndex < wordList.lastIndex

    val hasPrevious: Boolean
        get() = currentIndex > 0

    val position: Int
        get() = currentIndex + 1

    val total: Int
        get() = wordList.size

    fun updateFilter(filter: WordFilter) {
        _filter.value = filter
    }
}