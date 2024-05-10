package com.example.bangkit_2024_bpaai.Service.service

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class MyBoundService : Service() {
    private var binder = MyBinder()

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    // menggunakan LiveData untuk mengirim data countdown dari MyBoundService ke MainActivity
    val numberLiveData: MutableLiveData<Int> = MutableLiveData()

    // Kali ini, kita tidak menerapkan metode onStartCommand, melainkan metode onBinding
    // Hal ini karena bound service bersifat mengikat pada sebuah object, contohnya Activity
    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: ")

        serviceScope.launch {
            for (i in 1..50) {
                delay(1000)
                Log.d(TAG, "Do Something $i")
                numberLiveData.postValue(i)
            }
            Log.d(TAG, "Service dihentikan")
        }

        return binder
    }

    // untuk memberitahukan bahwa kelas pemanggil service terhapus dari dalam aplikasi
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "onUnbind: ")
        serviceJob.cancel()
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
        Log.d(TAG, "onRebind: ")
    }

    // untuk mengikat kelas service
    internal inner class MyBinder : Binder() {
        val getService: MyBoundService = this@MyBoundService
    }

    companion object {
        private val TAG = MyBoundService::class.java.simpleName
    }
}