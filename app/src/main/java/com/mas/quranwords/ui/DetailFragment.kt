package com.mas.quranwords.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mas.quranwords.R
import com.mas.quranwords.databinding.FragmentDetailBinding
import com.mas.quranwords.models.WordItem
import com.mas.quranwords.player.AudioPlayer
import com.mas.quranwords.qari.Reciter
import com.mas.quranwords.qari.Reciters
import com.mas.quranwords.ui.adapter.ReciterAdapter
import com.mas.quranwords.util.EXTRA_TYPE
import com.mas.quranwords.util.EXTRA_WORD
import com.mas.quranwords.util.UrlBuilder

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var selectedReciter = Reciters.ALL.first()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailBinding.bind(view)

        val type = arguments?.getString(EXTRA_TYPE)
        val wordItem = arguments?.getParcelable(EXTRA_WORD) as? WordItem

        if (wordItem == null) {
            Toast.makeText(requireContext(), "No item found", Toast.LENGTH_SHORT).show()
            return
        }

        showWord(wordItem)
    }

    private fun showWord(wordItem: WordItem) {
        binding.detailWordTextView.text =
            wordItem.word

        Glide.with(this)
            .load(UrlBuilder.buildImage(wordItem))
            .into(binding.detailImageView)

        initializeReciters()

        binding.playAudioButton.setOnClickListener {
            val audioUrl = UrlBuilder.buildAudio(selectedReciter, wordItem)
            AudioPlayer.play(audioUrl)
        }

        binding.reciterDropdown.setOnItemClickListener { parent, _, position, _ ->
            selectedReciter = parent.getItemAtPosition(position) as Reciter
        }
    }

    private fun initializeReciters() {
        val adapter = ReciterAdapter(requireContext(), Reciters.ALL)
        binding.reciterDropdown.setAdapter(adapter)
        binding.reciterDropdown.setText(selectedReciter.name, false)
    }

    override fun onDestroyView() {
        AudioPlayer.stop()
        _binding = null
        super.onDestroyView()
    }

    override fun onStop() {
        super.onStop()
        //AudioPlayer.stop()
    }
}