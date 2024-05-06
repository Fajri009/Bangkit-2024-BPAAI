package com.example.bangkit_2024_bpaai.AdvanceUI.CustomViewDariAwal

// untuk menggambarkan sebuah kursi dari aplikasi tiket
data class Seat(
    val id: Int,
    var x: Float? = 0F,
    var y: Float? = 0F,
    var name: String,
    var isBooked: Boolean
)