package com.example.bangkit_2024_bpaai.AdvanceUI.CustomView

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bangkit_2024_bpaai.R

class MainCustomView : AppCompatActivity() {
    private lateinit var myButton: MyButton
    private lateinit var myEditText: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)

        myButton = findViewById(R.id.my_button)
        myEditText = findViewById(R.id.my_edit_text)

        // Saat terdapat input maka secara otomatis myButton akan menjadi enable
        setMyButtonEnable()

        myEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        // Setelah menjadi enable, Anda bisa menampilkan Toast ketika klik myButton.
        myButton.setOnClickListener {
            Toast.makeText(this@MainCustomView, myEditText.text, Toast.LENGTH_SHORT).show()
        }
    }

    // Jika tidak ada teks maka my_button akan menjadi disable
    private fun setMyButtonEnable() {
        val result = myEditText.text
        myButton.isEnabled = result != null && result.toString().isNotEmpty()
    }
}