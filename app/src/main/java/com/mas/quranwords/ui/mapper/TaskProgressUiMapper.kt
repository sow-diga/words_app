package com.mas.quranwords.ui.mapper

import com.mas.quranwords.databinding.LayoutTaskProgressBinding
import com.mas.quranwords.models.TaskProgressState

fun LayoutTaskProgressBinding.renderState(state: TaskProgressState, animate: Boolean) {
    // 1. Progress indicators
    listenProgressIndicator.setProgress(state.listenCount, animate)
    repeatProgressIndicator.setProgress(state.repeatCount, animate)

    // 2. Text counts
    listenCountTextView.text = "${state.listenCount} / ${state.maxListen}"
    repeatCountTextView.text = "${state.repeatCount} / ${state.maxRepeat}"

    // 3. Phase headers & instructions
    when (state.currentPhase) {
        TaskProgressState.Phase.LISTENING -> {
            phaseHeaderTextView.text = "Phase 1: Listening"
            tapInstructionTextView.text =
                "Tap screen to count listening (${state.maxListen - state.listenCount} left)"
            setSectionAlpha(listenActive = true)
        }
        TaskProgressState.Phase.REPEATING -> {
            phaseHeaderTextView.text = "Phase 2: Repeating"
            tapInstructionTextView.text =
                "Tap screen to count repetitions (${state.maxRepeat - state.repeatCount} left)"
            setSectionAlpha(listenActive = false)
        }
        TaskProgressState.Phase.COMPLETED -> {
            phaseHeaderTextView.text = "🎉 Great job!"
            tapInstructionTextView.text = "You completed both tasks. Tap to reset."
            setSectionAlpha(listenActive = null) // Reset both to full opacity
        }
    }
}

/**
 * Helper extension to apply opacity dimming to inactive sections.
 */
private fun LayoutTaskProgressBinding.setSectionAlpha(listenActive: Boolean?) {
    val listenAlpha = if (listenActive == false) 0.4f else 1.0f
    val repeatAlpha = if (listenActive == true) 0.4f else 1.0f

    listenTitle.alpha = listenAlpha
    listenCountTextView.alpha = listenAlpha
    listenProgressIndicator.alpha = listenAlpha

    repeatTitle.alpha = repeatAlpha
    repeatCountTextView.alpha = repeatAlpha
    repeatProgressIndicator.alpha = repeatAlpha
}