package com.example.bangkit_2024_bpaai.AdvancedTesting.ui

import android.content.Context
import androidx.lifecycle.*
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.NewsRepository
import com.example.bangkit_2024_bpaai.AdvancedTesting.di.Injection
import com.example.bangkit_2024_bpaai.AdvancedTesting.ui.detail.NewsDetailViewModel
import com.example.bangkit_2024_bpaai.AdvancedTesting.ui.list.NewsViewModel

class ViewModelFactory private constructor(private val newsRepository: NewsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(newsRepository) as T
        } else if (modelClass.isAssignableFrom(NewsDetailViewModel::class.java)) {
            return NewsDetailViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}