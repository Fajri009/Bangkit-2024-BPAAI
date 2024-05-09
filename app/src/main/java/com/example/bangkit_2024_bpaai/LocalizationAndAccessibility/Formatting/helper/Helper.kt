package com.example.bangkit_2024_bpaai.LocalizationAndAccessibility.Formatting.helper

import java.text.*
import java.util.*

// untuk penomoran
fun String.withNumberingFormat(): String {
    return NumberFormat.getNumberInstance().format(this.toDouble())
}

// untuk mengonversi nilai string tersebut menjadi sebuah variabel date
fun String.withDateFormat(): String {
    // mengatur bagaimana nilai teks tanggal yang valid sebelum diubah menjadi date
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    // untuk mengubah nilai tanggal menjadi date
    val date = format.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(date)
}

// untuk perpindahan mata uang
fun String.withCurrencyFormat(): String {
    val rupiahExchangeRate = 12495.95
    val euroExchangeRate = 0.88

    var priceOnDollar = this.toDouble() / rupiahExchangeRate

    var mCurrencyFormat = NumberFormat.getCurrencyInstance()
    val deviceLocale = Locale.getDefault().country // mengetahui lokasi negara yang sedang digunakan pengguna

    when {
        deviceLocale.equals("ES") -> {
            priceOnDollar *= euroExchangeRate
        }
        deviceLocale.equals("ID") -> {
            priceOnDollar *= rupiahExchangeRate
        }
        else -> {
            mCurrencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
        }
    }
    return mCurrencyFormat.format(priceOnDollar)
}