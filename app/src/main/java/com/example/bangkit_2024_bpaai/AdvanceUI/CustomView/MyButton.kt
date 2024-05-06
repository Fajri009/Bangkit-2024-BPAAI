package com.example.bangkit_2024_bpaai.AdvanceUI.CustomView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.bangkit_2024_bpaai.R

class MyButton: AppCompatButton {
    constructor(context: Context) : super(context) // untuk di Activity/Fragment
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)  // untuk di XML

    /*
        constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) untuk di XML dengan style dari theme attribute
        constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) untuk di XML dengan style dari theme attribute dan/atau resource
     */

    private var txtColor: Int = 0
    private var enabledBackground: Drawable
    private var disabledBackground: Drawable

    /*
        Catatan
        Ketika Anda menggunakan CustomView, jangan memanggil resource di dalam metode onDraw() karena resource akan
        dipanggil terus menerus ketika activity berhasil dipanggil. Oleh karena itu, tempatkan pemanggilan resource
        ke dalam constructor.
     */

    init {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }

    // untuk mengcustom button ketika enable dan disable
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Mengubah background dari Button
        background = if (isEnabled) enabledBackground else disabledBackground

        // Mengubah warna text pada button
        setTextColor(txtColor)

        // Mengubah ukuran text pada button
        textSize = 12f

        // Menjadikan object pada button menjadi center
        gravity = Gravity.CENTER

        // Mengubah text pada button pada kondisi enable dan disable
        text = if (isEnabled) "Submit" else "Isi Dulu"
    }
}