package com.example.bangkit_2024_bpaai.LocalizationAndAccessibility.Accessibility

import android.content.Intent
import android.os.*
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bangkit_2024_bpaai.LocalizationAndAccessibility.Accessibility.data.ProductModel
import com.example.bangkit_2024_bpaai.LocalizationAndAccessibility.Accessibility.data.RemoteDataSource
import com.example.bangkit_2024_bpaai.LocalizationAndAccessibility.Accessibility.helper.withCurrencyFormat
import com.example.bangkit_2024_bpaai.LocalizationAndAccessibility.Accessibility.helper.withDateFormat
import com.example.bangkit_2024_bpaai.LocalizationAndAccessibility.Accessibility.helper.withNumberingFormat
import com.example.bangkit_2024_bpaai.R
import com.example.bangkit_2024_bpaai.databinding.ActivityMainAccessibilityBinding

class MainAccessibility : AppCompatActivity() {
    private lateinit var binding: ActivityMainAccessibilityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAccessibilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        setupData()

        // untuk menangani teks yang bersifat jamak
        val cupCount = 5
        val coffee = resources.getQuantityString(R.plurals.numberOfCupAvailable, cupCount, cupCount)
    }

    private fun setupAction() {
        binding.settingImageView.setOnClickListener {
            // untuk berpindah ke halaman pengaturan bahasa
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
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

    // untuk menjalan RemoteDataSource
    private fun setupData() {
        val data = RemoteDataSource(this)
        val product = data.getDetailProduct() // untuk menampung detail suatu produk

        product.apply {
            binding.apply {
                previewImageView.setImageResource(image)
                nameTextView.text = name
                storeTextView.text = store
                colorTextView.text = color
                sizeTextView.text = size
                descTextView.text = desc
                priceTextView.text = price.withCurrencyFormat()
                dateTextView.text = getString(R.string.dateFormat_formatting, date.withDateFormat())
                ratingTextView.text = getString(
                    R.string.ratingFormat_formatting,
                    rating.withNumberingFormat(),
                    countRating.withNumberingFormat()
                )
            }
        }

        setupAccessibility(product)
    }

    // menerapkan berbagai aksesibilitas yang diperlukan
    private fun setupAccessibility(productModel: ProductModel) {
        productModel.apply {
            binding.apply {
                settingImageView.contentDescription = getString(R.string.settingDescription)
                previewImageView.contentDescription = getString(R.string.previewDescription)
                colorTextView.contentDescription = getString(R.string.colorDescription, color)
                sizeTextView.contentDescription = getString(R.string.sizeDescription, size)
                ratingTextView.contentDescription = getString(
                    R.string.ratingDescription,
                    rating.withNumberingFormat(),
                    countRating.withNumberingFormat()
                )
                storeTextView.contentDescription = getString(R.string.storeDescription, store)
            }
        }
    }
}