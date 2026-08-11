package com.mas.quranwords.ui.words

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mas.quranwords.data.QuranRepository
import com.mas.quranwords.data.db.WordRecord
import com.mas.quranwords.databinding.ItemLocalWordBinding

class LocalWordAdapter(
    private val onClick: (WordRecord) -> Unit
) : ListAdapter<WordRecord, LocalWordAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLocalWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemLocalWordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(word: WordRecord) {
            with(binding) {
                wordText.text = word.word
                val surahText = QuranRepository.getSurahName(word.surahNumber)
                infoText.text = "${surahText} : ${word.ayahNumber}"
                root.setOnClickListener {
                    onClick(word)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<WordRecord>() {

        override fun areItemsTheSame(oldItem: WordRecord, newItem: WordRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WordRecord, newItem: WordRecord): Boolean {
            return oldItem == newItem
        }
    }
}