package com.example.bangkit_2024_bpaai.AdvancedTesting.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.NewsRepository
import org.junit.Assert.*
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.Result
import com.example.bangkit_2024_bpaai.AdvancedTesting.utils.DataDummy
import com.example.bangkit_2024_bpaai.AdvancedTesting.utils.MainDispatcherRule
import com.example.bangkit_2024_bpaai.AdvancedTesting.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewsDetailViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var newsRepository: NewsRepository
    private lateinit var newsDetailViewModel: NewsDetailViewModel
    private val dummyDetailNews = DataDummy.generateDummyNewsEntity()[0]

    @Before
    fun setUp() {
        newsDetailViewModel = NewsDetailViewModel(newsRepository)
        newsDetailViewModel.setNewsData(dummyDetailNews)
    }

//    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
//
//    @Before
//    fun setupDispatcher() {
//        Dispatchers.setMain(testDispatcher)
//    }
//
//    @After
//    fun tearDownDispatcher() {
//        Dispatchers.resetMain()
//    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when bookmarkStatus false Should call saveNews`() = runTest {
        val expectedBoolean = MutableLiveData<Boolean>()
        expectedBoolean.value = false

        `when`(newsRepository.isNewsBookmarked(dummyDetailNews.title)).thenReturn(expectedBoolean)

        newsDetailViewModel.bookmarkStatus.getOrAwaitValue()
        newsDetailViewModel.changeBookmark(dummyDetailNews)

        Mockito.verify(newsRepository).saveNews(dummyDetailNews)
    }

    @Test
    fun `when bookmarkStatus true Should call deleteNews`() = runTest {
        val expectedBoolean = MutableLiveData<Boolean>()
        expectedBoolean.value = true

        `when`(newsRepository.isNewsBookmarked(dummyDetailNews.title)).thenReturn(expectedBoolean)

        newsDetailViewModel.bookmarkStatus.getOrAwaitValue()
        newsDetailViewModel.changeBookmark(dummyDetailNews)

        Mockito.verify(newsRepository).deleteNews(dummyDetailNews.title)
    }
}