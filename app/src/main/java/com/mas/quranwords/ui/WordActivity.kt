package com.mas.quranwords.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.media3.common.*
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.mas.quranwords.R
import com.mas.quranwords.databinding.ActivityWordBinding
import com.mas.quranwords.models.WordItem
import com.mas.quranwords.qari.Reciter
import com.mas.quranwords.qari.Reciters
import com.mas.quranwords.ui.adapter.ReciterAdapter
import com.mas.quranwords.util.UrlBuilder
import com.mas.quranwords.util.EXTRA_WORD

class WordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordBinding
    private var player: ExoPlayer? = null
    private var selectedReciter = Reciters.ALL.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wordItem = intent.getSerializableExtra(EXTRA_WORD) as? WordItem

        if (wordItem != null) {
            binding.detailWordTextView.text = wordItem.word

            Glide.with(this)
                .load(UrlBuilder.buildImage(wordItem))
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.detailImageView)

            initializePlayer()
            initializeReciters()
            binding.playAudioButton.setOnClickListener {
                val audioUrl = UrlBuilder.buildAudio(
                    selectedReciter,
                    wordItem
                )

                playNetworkAudio(audioUrl)
            }

            binding.reciterDropdown.setOnItemClickListener { parent, _, position, _ ->
                selectedReciter = parent.getItemAtPosition(position) as Reciter
            }
        } else {
            Toast.makeText(this, "Failed to load word details", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build().apply {
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    Toast.makeText(this@WordActivity, "Media3 Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun playNetworkAudio(audioUrl: String) {
        player?.let { exoPlayer ->

            if (exoPlayer.currentMediaItem?.localConfiguration?.uri.toString() != audioUrl) {
                exoPlayer.setMediaItem(MediaItem.fromUri(audioUrl))
                exoPlayer.prepare()
            } else if (exoPlayer.playbackState == Player.STATE_ENDED) {
                exoPlayer.seekTo(0)
            }

            exoPlayer.play()
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.let {
            it.release()
            player = null
        }
    }

    private fun initializeReciters() {

        val adapter = ReciterAdapter(
            this,
            Reciters.ALL
        )

        binding.reciterDropdown.setAdapter(adapter)

        binding.reciterDropdown.setText(
            selectedReciter.name,
            false
        )
    }
}