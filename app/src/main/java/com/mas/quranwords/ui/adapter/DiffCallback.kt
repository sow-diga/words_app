package com.mas.quranwords.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mas.quranwords.models.WordItem

class DiffCallback : DiffUtil.ItemCallback<WordItem>() {

    override fun areItemsTheSame(
        oldItem: WordItem,
        newItem: WordItem
    ): Boolean {
        return oldItem.word == newItem.word
    }

    override fun areContentsTheSame(
        oldItem: WordItem,
        newItem: WordItem
    ): Boolean {
        return oldItem == newItem
    }
}