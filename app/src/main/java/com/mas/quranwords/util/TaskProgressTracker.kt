package com.mas.quranwords.util

import com.mas.quranwords.models.TaskProgressState

class TaskProgressTracker(
    private val maxListen: Int = 5,
    private val maxRepeat: Int = 10
) {
    var state = TaskProgressState(maxListen = maxListen, maxRepeat = maxRepeat)
        private set

    fun registerTap(): TaskProgressState {
        state = when {
            !state.isListenComplete -> state.copy(listenCount = state.listenCount + 1)
            !state.isRepeatComplete -> state.copy(repeatCount = state.repeatCount + 1)
            else -> TaskProgressState(maxListen = maxListen, maxRepeat = maxRepeat) // Reset
        }
        return state
    }

    fun reset() {
        state = TaskProgressState(maxListen = maxListen, maxRepeat = maxRepeat)
    }
}