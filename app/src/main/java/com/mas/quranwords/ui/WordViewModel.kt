package com.mas.quranwords.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.quranwords.models.ItemType
import com.mas.quranwords.models.WordItem
import com.mas.quranwords.network.RetrofitClient
import com.mas.quranwords.repository.WordRepository
import kotlinx.coroutines.launch

class WordViewModel : ViewModel() {
    private val repository = WordRepository(RetrofitClient.apiService)
    private val _words = MutableLiveData<List<WordItem>>()
    val words: LiveData<List<WordItem>> get() = _words
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private fun loadWords(
        request: suspend () -> Result<List<WordItem>>
    ) {

        viewModelScope.launch {

            _isLoading.value = true

            request()
                .onSuccess {
                    _words.value = it
                }
                .onFailure {
                    _errorMessage.value = it.message ?: "Unknown error"
                }

            _isLoading.value = false
        }
    }
    fun fetchWords(type: ItemType) {
        loadWords {
            when(type) {
                ItemType.WORD -> repository.getWords()
                ItemType.MEMORIZE -> repository.getMemorizeWords()
                ItemType.MISTAKE -> repository.getMistakes()
                ItemType.AYAH -> repository.getAyah()
                else -> Result.success(emptyList())
            }
        }
    }
}