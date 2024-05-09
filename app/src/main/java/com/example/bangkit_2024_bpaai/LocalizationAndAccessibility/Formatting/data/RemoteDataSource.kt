package com.example.bangkit_2024_bpaai.LocalizationAndAccessibility.Formatting.data

import android.content.Context
import com.example.bangkit_2024_bpaai.R

// untuk menyimulasikan proses pengambilan data dari API
// membutuhkan context untuk mengakses berkas string.xml
class RemoteDataSource(private val context: Context) {
    // untuk mendapatkan berbagai informasi mengenai produk
    fun getDetailProduct(): ProductModel {
        return ProductModel(
            name = context.getString(R.string.name_formatting),
            store = context.getString(R.string.store_formatting),
            size = context.getString(R.string.size_formatting),
            color = context.getString(R.string.color_formatting),
            desc = context.getString(R.string.desc_formatting),
            image = R.drawable.shoes,
            date = context.getString(R.string.date_formatting),
            rating = context.getString(R.string.rating_formatting),
            price = context.getString(R.string.price_formatting),
            countRating = context.getString(R.string.countRating_formatting)
        )
    }
}