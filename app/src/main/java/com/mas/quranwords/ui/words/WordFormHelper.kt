package com.mas.quranwords.ui.words

import android.content.Context
import android.widget.ArrayAdapter
import com.mas.quranwords.data.db.WordCategory
import com.mas.quranwords.data.db.WordLevel
import com.mas.quranwords.data.db.WordRecord
import com.mas.quranwords.databinding.FragmentAddWordBinding

class WordFormHelper(
    private val context: Context,
    private val binding: FragmentAddWordBinding
) {
    var category = WordCategory.WORD
        private set
    var level = WordLevel.MEDIUM
        private set

    fun setupDropdowns() {
        val categories = WordCategory.ALL
        binding.categoryDropdown.setAdapter(
            ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, categories)
        )
        binding.categoryDropdown.setOnClickListener {
            binding.categoryDropdown.showDropDown()
        }
        binding.categoryDropdown.setOnItemClickListener { _, _, position, _ ->
            category = categories[position]
        }

        val levels = WordLevel.ALL
        binding.levelDropdown.setAdapter(
            ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, levels)
        )
        binding.levelDropdown.setOnClickListener {
            binding.levelDropdown.showDropDown()
        }
        binding.levelDropdown.setOnItemClickListener { _, _, position, _ ->
            level = levels[position]
        }
    }

    fun fill(word: WordRecord) {
        binding.wordInput.setText(word.word)
        binding.englishInput.setText(word.english)
        binding.surahInput.setText(word.surahNumber.toString())
        binding.ayahInput.setText(word.ayahNumber.toString())
        binding.positionInput.setText(word.wordPosition.toString())
        binding.commentInput.setText(word.comment)
        setCategory(word.category)
        setLevel(word.level)
    }

    fun buildRecord(existing: WordRecord? = null): WordRecord {
        return if (existing == null) {
            WordRecord(
                word = binding.wordInput.text.toString().trim(),
                english = binding.englishInput.text.toString().trim(),
                surahNumber = binding.surahInput.text.toString().toIntOrNull() ?: 0,
                ayahNumber = binding.ayahInput.text.toString().toIntOrNull() ?: 0,
                wordPosition = binding.positionInput.text.toString().toIntOrNull() ?: 0,
                category = category,
                level = level,
                comment = binding.commentInput.text.toString()
            )
        } else {
            existing.copy(
                word = binding.wordInput.text.toString().trim(),
                english = binding.englishInput.text.toString().trim(),
                surahNumber = binding.surahInput.text.toString().toIntOrNull() ?: 0,
                ayahNumber = binding.ayahInput.text.toString().toIntOrNull() ?: 0,
                wordPosition = binding.positionInput.text.toString().toIntOrNull() ?: 0,
                category = category,
                level = level,
                comment = binding.commentInput.text.toString()
            )
        }
    }

    private fun setCategory(value: String) {
        category = value
        binding.categoryDropdown.setText(value, false)
    }

    private fun setLevel(value: String) {
        level = value
        binding.levelDropdown.setText(value, false)
    }
}