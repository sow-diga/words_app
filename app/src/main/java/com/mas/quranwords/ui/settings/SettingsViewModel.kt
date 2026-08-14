package com.mas.quranwords.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.quranwords.data.settings.SettingsRepository
import com.mas.quranwords.qari.Reciters
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val maxListen: Int = 5,
    val maxRepeat: Int = 10,
    val numbersSessionSize: Int = 20,
    val practiceReciter1: String = Reciters.AYMAN_SUWAID.folder!!,
    val practiceReciter2: String = Reciters.HUSARY.folder!!
)

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<SettingsUiState> =
        combine(
            repository.maxListen,
            repository.maxRepeat,
            repository.numbersSessionSize,
            repository.practiceReciter1,
            repository.practiceReciter2
        ) { maxListen, maxRepeat, numbersSessionSize, reciter1, reciter2 ->

            SettingsUiState(
                maxListen = maxListen,
                maxRepeat = maxRepeat,
                numbersSessionSize = numbersSessionSize,
                practiceReciter1 = reciter1,
                practiceReciter2 = reciter2
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

    fun setPracticeReciter1(folder: String) {
        viewModelScope.launch {
            repository.setPracticeReciter1(folder)
        }
    }

    fun setPracticeReciter2(folder: String) {
        viewModelScope.launch {
            repository.setPracticeReciter2(folder)
        }
    }
}