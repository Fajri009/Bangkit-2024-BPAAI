package com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.di

import android.content.Context
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.data.UserRepository
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.data.pref.UserPreference
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}