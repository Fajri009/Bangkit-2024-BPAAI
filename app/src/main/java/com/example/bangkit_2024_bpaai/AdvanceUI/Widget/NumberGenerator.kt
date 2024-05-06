package com.example.bangkit_2024_bpaai.AdvanceUI.Widget

import java.util.*

// Kelas NumberGenerator merupakan kelas helper yang memudahkan kita untuk membuat angka secara acak
internal object NumberGenerator {
    fun generate(max: Int): Int {
        val random = Random()
        return random.nextInt(max)
    }
}