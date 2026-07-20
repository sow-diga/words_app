package com.mas.quranwords.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

object AudioPlayer {

    private var player: ExoPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    private var loopEnabled = false
    private var loopDelayMs = 1000L
    private var currentUrl: String? = null
    private var playbackSpeed = 1.0f

    fun initialize(context: Context) {
        if (player != null) return

        player = ExoPlayer.Builder(context.applicationContext).build()

        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED && loopEnabled && currentUrl != null) {
                    handler.postDelayed({
                        play(currentUrl!!)
                    }, loopDelayMs)
                }
            }
        })
    }

    fun play(url: String) {
        currentUrl = url

        player?.apply {
            repeatMode = Player.REPEAT_MODE_OFF
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            setPlaybackSpeed(playbackSpeed)
            play()
        }
    }

    fun play(urls: List<String>) {
        val mediaItems = urls.map {
            MediaItem.fromUri(it)
        }

        player?.apply {
            setMediaItems(mediaItems)
            prepare()
            setPlaybackSpeed(playbackSpeed)
            play()
        }
    }

    fun pause() {
        player?.pause()
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
        player?.stop()
    }

    fun release() {
        handler.removeCallbacksAndMessages(null)
        player?.release()
        player = null
    }

    fun setLoop(enabled: Boolean) {
        player?.repeatMode =
            if (enabled)
                Player.REPEAT_MODE_ALL
            else
                Player.REPEAT_MODE_OFF
    }

    fun setSingleLoop(enabled: Boolean, delayMs: Long = 1000L) {
        loopEnabled = enabled
        loopDelayMs = delayMs
    }

    fun isPlaying() = player?.isPlaying == true

    /**
     * AudioPlayer.setSpeed(1.25f)
     */
    fun setSpeed(speed: Float = 0.75f) {
        playbackSpeed = speed
        player?.setPlaybackSpeed(speed)
    }
}