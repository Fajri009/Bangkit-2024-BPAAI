package com.example.bangkit_2024_bpaai.Media.ExoPlayerNotification.service

import androidx.media3.common.*
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.*

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        initializeSessionAndPlayer()
    }

    private fun initializeSessionAndPlayer() {
        val videoItem = MediaItem.Builder()
            .setUri("https://github.com/dicodingacademy/assets/releases/download/release-video/VideoDicoding.mp4")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("Bersama Dicoding, Kembangkan Dirimu Menjadi Talenta Digital Berstandar Global")
                    .setArtist("Dicoding")
                    .build()
            ).build()
        val audioItem = MediaItem.Builder()
            .setUri("https://github.com/dicodingacademy/assets/raw/main/android_intermediate_academy/bensound_ukulele.mp3")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("Ukulele")
                    .setArtist("Bensound")
                    .build()
            ).build()
        
        val player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            exoPlayer.setMediaItem(videoItem)
            exoPlayer.addMediaItem(audioItem)
            exoPlayer.prepare()
        }

        mediaSession = MediaSession.Builder(this, player).build()
    }

    // Fungsi ini berguna supaya klien lain bisa mengakses MediaSession ini
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }

        super.onDestroy()
    }
}