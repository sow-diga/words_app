package com.mas.quranwords.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

object AudioPlayer {

    private var player: ExoPlayer? = null

    fun initialize(context: Context) {
        if (player != null) return

        player = ExoPlayer.Builder(context.applicationContext).build()
    }

    fun play(url: String) {
        play(listOf(url))
    }

    fun play(urls: List<String>) {
        val mediaItems = urls.map {
            MediaItem.fromUri(it)
        }

        player?.apply {
            setMediaItems(mediaItems)
            prepare()
            play()
        }
    }

    fun pause() {
        player?.pause()
    }

    fun stop() {
        player?.stop()
    }

    fun release() {
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

    fun isPlaying() = player?.isPlaying == true

    /**
     * AudioPlayer.setSpeed(1.25f)
     */
    fun setSpeed(speed: Float) {
        player?.setPlaybackSpeed(speed)
    }
}