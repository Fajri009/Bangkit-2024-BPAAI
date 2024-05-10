package com.example.bangkit_2024_bpaai.Service

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bangkit_2024_bpaai.Service.service.*
import com.example.bangkit_2024_bpaai.databinding.ActivityMainServiceBinding

class MainService : AppCompatActivity() {
    private lateinit var binding: ActivityMainServiceBinding

    // permission untuk notifikasi
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean? ->
        if (!isGranted!!)
            Toast.makeText(this,
                "Unable to display Foreground service notification due to permission decline",
                Toast.LENGTH_LONG)
    }

    private var boundStatus = false
    private lateinit var boundService: MyBoundService

    // untuk menghubungkan antara BoundService dengan Activity
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val myBinder = service as MyBoundService.MyBinder
            boundService = myBinder.getService
            boundStatus = true
            getNumberFromService()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            boundStatus = false
        }
    }

    private fun getNumberFromService() {
        boundService.numberLiveData.observe(this) { number ->
            binding.tvBoundServiceNumber.text = number.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Background Service (dapat melakukan operasi yang tidak terlihat secara langsung oleh pengguna)
        val serviceIntent = Intent(this, MyBackgroundService::class.java)
        binding.btnStartBackgroundService.setOnClickListener {
            // Bukan startActivity(), tetapi startService() karena kita menginginkan sebuah service yang berjalan
            startService(serviceIntent)
        }
        binding.btnStopBackgroundService.setOnClickListener {
            // untuk mematikan service secara langsung dari luar kelas service
            stopService(serviceIntent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED)
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Foreground Service (dapat melakukan beberapa operasi yang terlihat oleh pengguna)
        val foregroundServiceIntent = Intent(this, MyForegroundService::class.java)
        binding.btnStartForegroundService.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 26) { //  Jika menggunakan API 26 ke atas
                startForegroundService(foregroundServiceIntent)
            } else {
                startService(foregroundServiceIntent)
            }

            // Cara sederhana
            // ContextCompat.startForegroundService(this, foregroundServiceIntent)
        }
        binding.btnStopForegroundService.setOnClickListener {
            stopService(foregroundServiceIntent)
        }

        // Bound Service (berjalan dengan terikat pada object tertentu)
        val boundServiceIntent = Intent(this, MyBoundService::class.java)
        binding.btnStartBoundService.setOnClickListener {
            bindService(boundServiceIntent, connection, BIND_AUTO_CREATE)
            // connection merupakan sebuah ServiceConnection yang berfungsi sebagai callback dari kelas MyBoundService
            // BIND_AUTO_CREATE yang membuat sebuah service jika service tersebut belum aktif

            /*
                BIND_ABOVE_CLIENT: digunakan ketika sebuah service lebih penting daripada aplikasi itu sendiri.
                BIND_ADJUST_WITH_ACTIVITY: saat mengikat sebuah service dari activity, ia akan mengizinkan untuk menargetkan service mana yang lebih penting berdasarkan activity yang terlihat oleh pengguna.
                BIND_ALLOW_OOM_MANAGEMENT: memungkinkan untuk mengikat service hosting untuk mengatur memori secara normal.
                BIND_AUTO_CREATE: secara otomatis membuat service selama binding-nya aktif.
                BIND_DEBUG_UNBIND: berfungsi sebagai bantuan ketika debug mengalami masalah pada pemanggilan unBind.
                BIND_EXTERNAL_SERVICE: merupakan service yang terikat dengan service eksternal yang terisolasi.
                BIND_IMPORTANT: service ini sangat penting bagi klien, jadi harus dibawa ke tingkat proses foreground.
                BIND_NOT_FOREGROUND: pada service ini tak disarankan untuk mengubah ke tingkat proses foreground.
                BIND_WAIVE_PRIORITY: service ini tidak akan mempengaruhi penjadwalan atau prioritas manajemen memori dari target proses layanan hosting.
             */
        }
        binding.btnStopBoundService.setOnClickListener {
            unbindService(connection) // secara tidak langsung akan memanggil metode onUnbind yang ada di kelas MyBoundService
        }
    }

    // Agar bound service ikut berhenti ketika Activity dalam keadaan stopped, kita juga perlu melepaskan service tersebut dalam state onStop
    override fun onStop() {
        super.onStop()
        if (boundStatus) {
            unbindService(connection) // pelepasan service dari Activity
            boundStatus = false
        }
    }
}