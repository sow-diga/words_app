package com.mas.quranwords.ui.words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mas.quranwords.data.repository.LocalWordRepository

class LocalWordViewModelFactory(
    private val repository: LocalWordRepository
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocalWordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocalWordViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}