package com.mas.quranwords.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mas.quranwords.databinding.ViewWordsBottomBarBinding

class WordStudyBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val binding =
        ViewWordsBottomBarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        orientation = VERTICAL
    }

    fun setPosition(current: Int, total: Int) {
        binding.positionText.text = "$current / $total"
    }

    fun setExpanded(expanded: Boolean) {
        binding.navigationButton.setImageResource(
            if (expanded)
                com.mas.quranwords.R.drawable.expand_less
            else
                com.mas.quranwords.R.drawable.expand_more
        )
        showEditButtons(expanded)
    }

    fun setOnEditClick(listener: OnClickListener) {
        binding.editButton.setOnClickListener(listener)
    }

    fun setOnDeleteClick(listener: OnClickListener) {
        binding.deleteButton.setOnClickListener(listener)
    }

    fun setOnNavigationClick(listener: OnClickListener) {
        binding.navigationButton.setOnClickListener(listener)
    }

    private fun showEditButtons(show: Boolean) {
        binding.editButton.isEnabled = show
        binding.deleteButton.isEnabled = show

        binding.editButton.animate()
            .alpha(if (show) 1f else 0f)
            .setDuration(150)
            .start()

        binding.deleteButton.animate()
            .alpha(if (show) 1f else 0f)
            .setDuration(150)
            .start()
    }
}