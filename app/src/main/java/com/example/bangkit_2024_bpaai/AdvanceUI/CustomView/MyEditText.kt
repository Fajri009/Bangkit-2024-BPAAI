package com.example.bangkit_2024_bpaai.AdvanceUI.CustomView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.bangkit_2024_bpaai.R

class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {
    private var clearButtonImage: Drawable

    init {
        // Menginisialisasi gambar clear button
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable

        // Menambahkan aksi kepada clear button
        setOnTouchListener(this)

        // Menambahkan aksi ketika ada perubahan text akan memunculkan clear button
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            // ketika teks di dalam EditText berubah (onTextChanged) tombol silang akan kembali ditampilkan dengan fungsi showClearButton dan dihilangkan jika teks kosong dengan fungsi hideClearButton
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    // untuk mengatur attribute pada kelas ini
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan nama Anda"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    // untuk aksi ketika tombol clear
    private fun showClearButton() {
        // pada showClearButton kita memasukkan gambar pada parameter ketiga karena kita ingin menampilkan gambar pada sebelah kanan EditText,
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        // pada hideClearButton diisi dengan null semua untuk menghilangkan gambar
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    // untuk menentukan behaviour dari komponen Custom View ketika komponen ditekan
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            // terdapat pengecekan jenis handphone apakah menggunakan format RTL (Right-to-left) seperti bahasa Arab atau format LTR (Left-to-right) seperti bahasa Indonesia/Inggris
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }

            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }
}