package com.mas.quranwords.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.quranwords.data.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val maxListen: Int = 5,
    val maxRepeat: Int = 10,
    val numbersSessionSize: Int = 20
)

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<SettingsUiState> =
        combine(
            repository.maxListen,
            repository.maxRepeat,
            repository.numbersSessionSize
        ) { maxListen, maxRepeat, numbersSessionSize ->

            SettingsUiState(
                maxListen = maxListen,
                maxRepeat = maxRepeat,
                numbersSessionSize = numbersSessionSize
            )

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    fun setMaxListen(value: Int) {
        viewModelScope.launch {
            repository.setMaxListen(value)
        }
    }

    fun setMaxRepeat(value: Int) {
        viewModelScope.launch {
            repository.setMaxRepeat(value)
        }
    }

    fun setNumbersSessionSize(value: Int) {
        viewModelScope.launch {
            repository.setNumbersSessionSize(value)
        }
    }
}