package com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.view.main

import android.animation.*
import android.content.Intent
import android.os.*
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.view.ViewModelFactory
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.view.welcome.WelcomeActivity
import com.example.bangkit_2024_bpaai.databinding.ActivityMainPropertyAnimationBinding

class MainPropertyAnimation : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainPropertyAnimationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPropertyAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

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
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration =  6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA,  1f).setDuration(1000)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(1000)
        val logout = ObjectAnimator.ofFloat(binding.logoutButton, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playSequentially(name, message, logout)
            startDelay = 100
            start()
        }
    }
}