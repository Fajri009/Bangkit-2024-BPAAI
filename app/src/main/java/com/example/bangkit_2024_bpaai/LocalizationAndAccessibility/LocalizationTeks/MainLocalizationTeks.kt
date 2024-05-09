package com.example.bangkit_2024_bpaai.LocalizationAndAccessibility.LocalizationTeks

import android.content.Intent
import android.os.*
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bangkit_2024_bpaai.R
import com.example.bangkit_2024_bpaai.databinding.ActivityMainLocalizationTeksBinding

class MainLocalizationTeks : AppCompatActivity() {
    private lateinit var binding: ActivityMainLocalizationTeksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLocalizationTeksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        // untuk menangani teks yang bersifat jamak
        val cupCount = 5
        val coffee = resources.getQuantityString(R.plurals.numberOfCupAvailable, cupCount, cupCount)
    }

    private fun setupAction() {
        binding.settingImageView.setOnClickListener {
            // untuk berpindah ke halaman pengaturan bahasa
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}