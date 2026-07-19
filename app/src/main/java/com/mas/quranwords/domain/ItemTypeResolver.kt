package com.mas.quranwords.domain

import com.mas.quranwords.models.ItemType
import com.mas.quranwords.ui.WordViewModel

object ItemTypeResolver {

    fun execute(itemType: ItemType, viewModel: WordViewModel) {
        when (itemType) {
            ItemType.WORD,
            ItemType.MEMORIZE,
            ItemType.MISTAKE,
            ItemType.AYAH  -> {
                viewModel.fetchWords(itemType)
            }
            ItemType.NUMBERS -> Unit
        }
    }
}