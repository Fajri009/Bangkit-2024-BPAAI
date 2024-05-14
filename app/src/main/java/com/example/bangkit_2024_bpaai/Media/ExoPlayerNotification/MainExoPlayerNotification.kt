package com.example.bangkit_2024_bpaai.Media.ExoPlayerNotification

import android.content.ComponentName
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.*
import com.example.bangkit_2024_bpaai.Media.ExoPlayerNotification.service.PlaybackService
import com.example.bangkit_2024_bpaai.databinding.ActivityMainExoPlayerNotificationBinding
import com.google.common.util.concurrent.MoreExecutors

class MainExoPlayerNotification : AppCompatActivity() {
    private lateinit var binding: ActivityMainExoPlayerNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainExoPlayerNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoItem = MediaItem.fromUri("https://github.com/dicodingacademy/assets/releases/download/release-video/VideoDicoding.mp4")
        val audioItem = MediaItem.fromUri("https://github.com/dicodingacademy/assets/raw/main/android_intermediate_academy/bensound_ukulele.mp3")

        val player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            exoPlayer.setMediaItem(videoItem)
            exoPlayer.addMediaItem(audioItem)
            exoPlayer.prepare()
        }
        binding.playerView.player = player

        hideSystemUI()
    }

    override fun onStart() {
        super.onStart()
        // SessionToken berfungsi untuk menghubungkan MediaController dengan MediaSessionService
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        // MediaController adalah sebuah calss yang berfungsi untuk menghubungkan PlayerView dengan MediaSession
        // Berkat kelas inilah ketika Anda menekan tombol pada notifikasi, audio juga akan bereaksi, seperti ketikan menakan tombol play, pause, next, dan back
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                binding.playerView.player = controllerFuture.get()
            },
            MoreExecutors.directExecutor()
        )
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.playerView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}