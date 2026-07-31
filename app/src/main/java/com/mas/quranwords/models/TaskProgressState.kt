package com.mas.quranwords.models

data class TaskProgressState(
    val listenCount: Int = 0,
    val maxListen: Int = 5,
    val repeatCount: Int = 0,
    val maxRepeat: Int = 10
) {
    val isListenComplete: Boolean
        get() = listenCount >= maxListen

    val isRepeatComplete: Boolean
        get() = repeatCount >= maxRepeat

    val isPhase1Complete: Boolean
        get() = listenCount >= maxListen

    val isFullyComplete: Boolean
        get() = isListenComplete && isRepeatComplete

    val currentPhase: Phase
        get() = when {
            !isListenComplete -> Phase.LISTENING
            !isRepeatComplete -> Phase.REPEATING
            else -> Phase.COMPLETED
        }

    enum class Phase {
        LISTENING,
        REPEATING,
        COMPLETED
    }
}