package com.example.bangkit_2024_bpaai.AdvancedTesting.ui.detail

import androidx.lifecycle.*
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.NewsRepository
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.local.entity.NewsEntity
import kotlinx.coroutines.launch

class NewsDetailViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val newsData = MutableLiveData<NewsEntity>()

    fun setNewsData(news: NewsEntity) {
        newsData.value = news
    }

    val bookmarkStatus = newsData.switchMap {
        newsRepository.isNewsBookmarked(it.title)
    }

    fun changeBookmark(newsDetail: NewsEntity) {
        viewModelScope.launch {
            if (bookmarkStatus.value as Boolean) {
                newsRepository.deleteNews(newsDetail.title)
            } else {
                newsRepository.saveNews(newsDetail)
            }
        }
    }
}