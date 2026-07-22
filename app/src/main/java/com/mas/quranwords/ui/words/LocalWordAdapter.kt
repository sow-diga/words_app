package com.mas.quranwords.ui.words

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mas.quranwords.data.db.WordRecord
import com.mas.quranwords.databinding.ItemLocalWordBinding

class LocalWordAdapter(
    private val onClick: (WordRecord) -> Unit
) :
    RecyclerView.Adapter<LocalWordAdapter.ViewHolder>() {

    private val items = mutableListOf<WordRecord>()

    fun submitList(words: List<WordRecord>) {
        items.clear()
        items.addAll(words)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLocalWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: ItemLocalWordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(word: WordRecord) {
            binding.wordText.text = word.word
            binding.infoText.text = "Surah ${word.surahNumber} : ${word.ayahNumber}"
            binding.root.setOnClickListener {
                onClick(word)
            }
        }
    }
}