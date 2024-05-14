package com.example.bangkit_2024_bpaai.Media.MediaPlayer

import android.media.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bangkit_2024_bpaai.R
import com.example.bangkit_2024_bpaai.databinding.ActivityMainMediaPlayerBinding
import java.io.IOException

class MainMediaPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityMainMediaPlayerBinding
    private var mMediaPlayer: MediaPlayer? = null
    private var isReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        play()
        stop()
    }

    private fun play() {
        binding.btnPlay.setOnClickListener {
            if (!isReady) {
                // prepareAsync() berfungsi untuk menyiapkan MediaPlayer jika belum disiapkan atau diperbarui
                mMediaPlayer?.prepareAsync()
            } else {
                if (mMediaPlayer?.isPlaying as Boolean) {
                    mMediaPlayer?.pause()
                } else {
                    mMediaPlayer?.start()
                }
            }
        }
    }

    private fun stop() {
        // untuk mengirim perintah stop
        binding.btnStop.setOnClickListener {
            if (mMediaPlayer?.isPlaying as Boolean || isReady) {
                mMediaPlayer?.stop()
                isReady = false
            }
        }
    }

    // method ini berfungsi untuk menginisialisasi mediaplayer
    private fun init() {
        mMediaPlayer = MediaPlayer()
        val attribute = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        mMediaPlayer?.setAudioAttributes(attribute)

        //
        val afd = applicationContext.resources.openRawResourceFd(R.raw.jkt48_ingatan_cosmos)
        try {
            // setDataSource berfungsi untuk memasukkan detail informasi dari asset atau musik yang akan diputar
            mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mMediaPlayer?.setOnPreparedListener {
            isReady = true
            mMediaPlayer?.start()
        }
        mMediaPlayer?.setOnErrorListener { _, _, _ -> false }
    }
}