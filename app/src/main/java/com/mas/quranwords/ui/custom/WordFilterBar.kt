package com.mas.quranwords.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.mas.quranwords.data.QuranRepository
import com.mas.quranwords.data.db.WordCategory
import com.mas.quranwords.data.db.WordLevel
import com.mas.quranwords.databinding.LayoutWordFilterBarBinding
import com.mas.quranwords.domain.filter.WordFilter

class WordFilterBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

    private val binding =
        LayoutWordFilterBarBinding.inflate(LayoutInflater.from(context), this, true)

    private var listener: ((WordFilter) -> Unit)? = null

    private var category: String? = null
    private var surah: Int? = null
    private var level: String? = null
    private lateinit var surahAdapter: ArrayAdapter<String>
    private var initialized = false

    init {
        setupClear()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!initialized) {
            initialized = true
            setupCategory()
            setupLevel()
            setupSurah()
        }
    }

    fun setOnFilterChangedListener(listener: (WordFilter) -> Unit) {
        this.listener = listener
    }

    private fun notifyChanged() {
        listener?.invoke(
            WordFilter(category = category, surah = surah, level = level)
        )
    }

    private fun setupCategory() {
        val categories = listOf("All") + WordCategory.ALL
        binding.categoryDropdown.apply {
            setAdapter(ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, categories))
            setText("All", false)
            setOnItemClickListener { _, _, position, _ ->
                category =
                    if (position == 0)
                        null
                    else
                        categories[position]

                notifyChanged()
            }
        }
    }

    private fun setupLevel() {
        val levels = listOf("All") + WordLevel.ALL
        binding.levelDropdown.apply {
            setAdapter(ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, levels))
            setText("All", false)
            setOnItemClickListener { _, _, position, _ ->
                level = if (position == 0) null else levels[position]
                notifyChanged()
            }
        }
    }

    private fun setupSurah() {
        val surahItems = buildList {
            add("All")
            for (surah in 1..114) {
                add("$surah - ${QuranRepository.getSurahName(surah)}")
            }
        }

        surahAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            surahItems
        )

        binding.surahDropdown.apply {
            setAdapter(surahAdapter)
            setText("All", false)
            setOnItemClickListener { _, _, position, _ ->
                surah = if (position == 0) null else position
                notifyChanged()
            }
        }
    }

    private fun setupClear() {
        binding.clearButton.setOnClickListener {
            category = null
            surah = null
            level = null

            binding.categoryDropdown.setText("All", false)
            binding.surahDropdown.setText("All", false)
            binding.levelDropdown.setText("All", false)
            notifyChanged()
        }
    }

    fun setFilter(filter: WordFilter) {
        category = filter.category
        surah = filter.surah
        level = filter.level

        with(binding) {
            categoryDropdown.setText(category ?: "All", false)
            surahDropdown.setText(surah?.let { "$it - ${QuranRepository.getSurahName(it)}" } ?: "All", false)
            levelDropdown.setText(level ?: "All", false)
        }
    }
}