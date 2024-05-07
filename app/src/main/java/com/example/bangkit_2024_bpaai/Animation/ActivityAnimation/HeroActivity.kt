package com.example.bangkit_2024_bpaai.Animation.ActivityAnimation

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bangkit_2024_bpaai.Animation.ActivityAnimation.model.Hero
import com.example.bangkit_2024_bpaai.R

class HeroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero)
        setupData()
    }

    private fun setupData() {
        val hero = intent.getParcelableExtra<Hero>("Hero") as Hero
        Glide.with(applicationContext)
            .load(hero.photo)
            .circleCrop()
            .into(findViewById(R.id.profileImageView))
        findViewById<TextView>(R.id.nameTextView).text = hero.name
        findViewById<TextView>(R.id.descTextView).text = hero.description
    }
}