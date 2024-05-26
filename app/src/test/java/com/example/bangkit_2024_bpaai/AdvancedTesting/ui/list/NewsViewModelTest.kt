package com.example.bangkit_2024_bpaai.AdvancedTesting.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.NewsRepository
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.local.entity.NewsEntity
import com.example.bangkit_2024_bpaai.AdvancedTesting.utils.DataDummy
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.Result
import com.example.bangkit_2024_bpaai.AdvancedTesting.utils.getOrAwaitValue
import com.example.bangkit_2024_bpaai.AdvancedTesting.ui.list.NewsViewModel
import org.junit.Rule
import org.mockito.Mockito

@RunWith(MockitoJUnitRunner::class) // Untuk kelas yang menggunakan annotation @Mock dari Mockito, kita wajib menggunakan annotation @RunWith(MockitoJUnitRunner::class)
// MockitoJUnitRunner merupakan kelas yang digunakan untuk membuat dan memvalidasi proses mock
class NewsViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // untuk pengujian LiveData

    @Mock
    private lateinit var newsRepository : NewsRepository
    private lateinit var newsViewModel: NewsViewModel
    private val dummyNews = DataDummy.generateDummyNewsEntity()

    @Before
    fun setUp() {
        newsViewModel = NewsViewModel(newsRepository)
    }

    @Test
    fun `when Get HeadlineNews Should Not Null and Return Success`() {
        val expectedNews = MutableLiveData<Result<List<NewsEntity>>>()
        expectedNews.value = Result.Success(dummyNews)

        `when`(newsRepository.getHeadlineNews()).thenReturn(expectedNews)
        // Ketika newsRepository.getHeadlineNews dipanggil, maka akan mengembalikan nilai dari expectedNews

        val actualNews = newsViewModel.getHeadlineNews().getOrAwaitValue()
        Mockito.verify(newsRepository).getHeadlineNews()
        Assert.assertNotNull(actualNews)
        Assert.assertTrue(actualNews is Result.Success)
        Assert.assertEquals(dummyNews.size, (actualNews as Result.Success).data.size)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val headlineNews = MutableLiveData<Result<List<NewsEntity>>>()
        headlineNews.value = Result.Error("Error")

        `when`(newsRepository.getHeadlineNews()).thenReturn(headlineNews)

        val actualNews = newsViewModel.getHeadlineNews().getOrAwaitValue()
        Mockito.verify(newsRepository).getHeadlineNews()
        Assert.assertNotNull(actualNews)
        Assert.assertTrue(actualNews is Result.Error)
    }
}
