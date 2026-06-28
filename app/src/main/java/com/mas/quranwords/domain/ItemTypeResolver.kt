package com.mas.quranwords.domain

import com.mas.quranwords.models.ItemType
import com.mas.quranwords.ui.WordViewModel

object ItemTypeResolver {

    fun execute(itemType: ItemType, viewModel: WordViewModel) {
        when (itemType) {
            ItemType.WORD -> viewModel.fetchWords()
            ItemType.MEMORIZE -> viewModel.fetchMemorizeWords()
            ItemType.MISTAKE -> viewModel.fetchMistakes()
            ItemType.AYAH -> viewModel.fetchAyah()
            ItemType.NUMBERS -> Unit
        }
    }
}