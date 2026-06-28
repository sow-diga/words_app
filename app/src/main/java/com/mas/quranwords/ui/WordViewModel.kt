package com.mas.quranwords.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun fetchWords() {

        viewModelScope.launch {
            _isLoading.value = true
            repository.getWords()
                .onSuccess { words ->
                    _words.value = words
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _isLoading.value = false
                    _errorMessage.value = exception.message ?: "Unknown error"
                }
        }
    }

    fun fetchMemorizeWords() {

        viewModelScope.launch {
            _isLoading.value = true
            repository.getMemorizeWords()
                .onSuccess { words ->
                    _words.value = words
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _isLoading.value = false
                    _errorMessage.value = exception.message ?: "Unknown error"
                }
        }
    }

    fun fetchMistakes() {

        viewModelScope.launch {
            _isLoading.value = true
            repository.getMistakes()
                .onSuccess { words ->
                    _words.value = words
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _isLoading.value = false
                    _errorMessage.value = exception.message ?: "Unknown error"
                }
        }
    }

    fun fetchAyah() {

        viewModelScope.launch {
            _isLoading.value = true
            repository.getAyah()
                .onSuccess { words ->
                    _words.value = words
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _isLoading.value = false
                    _errorMessage.value = exception.message ?: "Unknown error"
                }
        }
    }
}