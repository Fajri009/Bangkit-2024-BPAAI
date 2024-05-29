package com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.adapter.LoadingStateAdapter
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.adapter.QuoteListAdapter
import com.example.bangkit_2024_bpaai.R
import com.example.bangkit_2024_bpaai.databinding.ActivityMainPaging3Binding

class MainPaging3 : AppCompatActivity() {
    private lateinit var binding: ActivityMainPaging3Binding
    private val viewModel: MainPaging3ViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPaging3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvQuote.layoutManager = LinearLayoutManager(this)

        getData()
    }

//    private fun getData() {
//        val adapter = QuoteListAdapter()
//        binding.rvQuote.adapter = adapter
//        viewModel.getQuote()
//        viewModel.quote.observe(this) {
//            adapter.submitList(it)
//        }
//    }

    private fun getData() {
        val adapter = QuoteListAdapter()
        binding.rvQuote.adapter = adapter.withLoadStateFooter( // withLoadStateFooter digunakan untuk menampilkan loading pada footer (bagian bawah)
            // sedangkan withLoadStateHeader untuk menampilkan loading pada header (bagian atas)
            // withLoadStateHeaderAndFooter juga dapat digunakan untuk menampilkan loading di keduanya
            footer = LoadingStateAdapter {
                adapter.retry() // fungsi ini akan mengulang kembali fungsi load yang sebelumnya gagal
            }
        )
        viewModel.quote.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
}