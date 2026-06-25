package com.mas.quranwords.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.mas.quranwords.R
import com.mas.quranwords.databinding.FragmentDetailBinding
import com.mas.quranwords.models.WordItem
import com.mas.quranwords.qari.Reciter
import com.mas.quranwords.qari.Reciters
import com.mas.quranwords.ui.adapter.ReciterAdapter
import com.mas.quranwords.util.EXTRA_TYPE
import com.mas.quranwords.util.EXTRA_WORD
import com.mas.quranwords.util.UrlBuilder

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var player: ExoPlayer? = null

    private var selectedReciter =
        Reciters.ALL.first()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailBinding.bind(view)

        val type = arguments?.getString(EXTRA_TYPE)
        val wordItem = arguments?.getParcelable(EXTRA_WORD) as? WordItem

        if (wordItem == null) {
            Toast.makeText(
                requireContext(),
                "No item found",
                Toast.LENGTH_SHORT
            ).show()

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

        initializePlayer()
        initializeReciters()

        binding.playAudioButton.setOnClickListener {

            val audioUrl =
                UrlBuilder.buildAudio(
                    selectedReciter,
                    wordItem
                )

            playNetworkAudio(audioUrl)
        }

        binding.reciterDropdown.setOnItemClickListener {
                parent,
                _,
                position,
                _ ->

            selectedReciter =
                parent.getItemAtPosition(position)
                        as Reciter
        }
    }

    private fun initializePlayer() {

        player =
            ExoPlayer.Builder(requireContext())
                .build()
                .apply {

                    addListener(
                        object : Player.Listener {

                            override fun onPlayerError(
                                error: PlaybackException
                            ) {

                                Toast.makeText(
                                    requireContext(),
                                    error.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
    }

    private fun playNetworkAudio(audioUrl: String) {

        player?.let { exoPlayer ->

            exoPlayer.setMediaItem(
                MediaItem.fromUri(audioUrl)
            )

            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    private fun initializeReciters() {

        val adapter =
            ReciterAdapter(
                requireContext(),
                Reciters.ALL
            )

        binding.reciterDropdown.setAdapter(adapter)

        binding.reciterDropdown.setText(
            selectedReciter.name,
            false
        )
    }

    override fun onDestroyView() {

        player?.release()
        player = null

        _binding = null

        super.onDestroyView()
    }
}