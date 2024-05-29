package com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.adapter.QuoteListAdapter
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.data.QuoteRepository
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.network.QuoteResponseItem
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.utils.DataDummy
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.utils.MainDispatcherRule
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    // InstantTaskExecutorRule : rule ini digunakan supaya background process dari Architecture Component seperti LiveData dapat dijalankan secara synchronous
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    // MainDispatcherRule : custom rule yang digunakan untuk menjalankan coroutine di dalam testing dengan memanfaatkan TestDispatcher
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var quoteRepository: QuoteRepository

    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {
        val dummyQuote = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<QuoteResponseItem> = QuotePagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<QuoteResponseItem>>()
        expectedQuote.value = data

        Mockito.`when`(quoteRepository.getQuote()).thenReturn(expectedQuote)
        val mainViewModel = MainPaging3ViewModel(quoteRepository)
        val actualQuote: PagingData<QuoteResponseItem> = mainViewModel.quote.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = QuoteListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<QuoteResponseItem> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<QuoteResponseItem>>()
        expectedQuote.value = data
        Mockito.`when`(quoteRepository.getQuote()).thenReturn(expectedQuote)

        val mainViewModel = MainPaging3ViewModel(quoteRepository)
        val actualQuote: PagingData<QuoteResponseItem> = mainViewModel.quote.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = QuoteListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}
class QuotePagingSource : PagingSource<Int, LiveData<List<QuoteResponseItem>>>() {
    companion object {
        fun snapshot(items: List<QuoteResponseItem>): PagingData<QuoteResponseItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<QuoteResponseItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<QuoteResponseItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

