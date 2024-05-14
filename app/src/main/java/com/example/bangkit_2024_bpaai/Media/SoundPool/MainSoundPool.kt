package com.example.bangkit_2024_bpaai.Media.SoundPool

import android.media.*
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bangkit_2024_bpaai.R
import com.example.bangkit_2024_bpaai.databinding.ActivityMainSoundPoolBinding

class MainSoundPool : AppCompatActivity() {
    private lateinit var binding: ActivityMainSoundPoolBinding

    private lateinit var sp: SoundPool
    private var soundId: Int = 0
    private var spLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSoundPoolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSound()
        soundPool()
    }

    private fun buttonSound() {
        binding.btnSoundPool.setOnClickListener {
            if (spLoaded) {
                sp.play(soundId, 1f, 1f, 0, 0, 1f)
                /*
                    Paremeter loop digunakan untuk mengulang audio ketika telah selesai dimainkan.
                    Nilai -1 menunjukkan bahwa audio akan diulang-ulang tanpa berhenti.
                    Nilai 0 menunjukkan audio akan dimainkan sekali.
                    Nilai 1 menunjukkan audio akan dimainkan sebanyak 2 kali.
                 */
                /*
                    Parameter rate mempunyai range dari 0.5 - 2.0.
                    Rate 1.0 akan memainkan audio secara normal,
                    0.5 akan memainkan audio dengan kecepatan setengah,
                    dan 2.0 akan memainkan audio 2 kali lipat lebih cepat
                 */
            }
        }
    }

    private fun soundPool() {
        sp = SoundPool.Builder()
            // angka 10 untuk menentukan jumlah streams secara simultan yang dapat diputar secara bersamaan
            .setMaxStreams(10)
            .build()
        // SoundPool hanya bisa memainkan berkas yang telah dimuat secara sempurna
        // untuk memastikan bahwa proses pemuatan telah selesai, gunakan listener
        sp.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                // mengetahui apakah pemuatan berkas audio sudah selesai atau belum
                spLoaded = true
            } else {
                Toast.makeText(this@MainSoundPool, "Gagal load", Toast.LENGTH_SHORT).show()
            }
        }

        soundId = sp.load(this, R.raw.coach_justin_alfachri_gblk, 1)
    }
}