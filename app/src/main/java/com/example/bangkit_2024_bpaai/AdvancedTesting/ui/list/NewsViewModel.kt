package com.example.bangkit_2024_bpaai.AdvancedTesting.ui.list

import androidx.lifecycle.ViewModel
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun getHeadlineNews() = newsRepository.getHeadlineNews()

    fun getBookmarkedNews() = newsRepository.getBookmarkedNews()
}