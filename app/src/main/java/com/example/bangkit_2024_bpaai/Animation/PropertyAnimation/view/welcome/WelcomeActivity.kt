package com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.view.welcome

import android.animation.*
import android.content.Intent
import android.os.*
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.view.login.LoginActivity
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.view.signup.SignupActivity
import com.example.bangkit_2024_bpaai.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
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

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun playAnimation() {
        // menambahkan Animasi pada gambar yang berjalan ke kanan dan ke kiri secara berulang
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE // untuk membuat animasi terus berjalan
            repeatMode = ObjectAnimator.REVERSE // agar bisa kembali ke titik semula
        }.start()

        // menjalankan animasi untuk beberapa item di bawah gambar, yakni 2 TextView dan 2 Button
        // ALPHA untuk fade in
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(1000)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(1000)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(1000)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(1000)

        // animasi berjalan secara bersamaan
        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        // animasi berjalan secara bergantian
        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }

        // Cara lain
        /*
            AnimatorSet().apply {
                play(login).with(signup)
                play(login).after(desc)
                play(title).before(desc)
                start()
            }
         */
    }
}