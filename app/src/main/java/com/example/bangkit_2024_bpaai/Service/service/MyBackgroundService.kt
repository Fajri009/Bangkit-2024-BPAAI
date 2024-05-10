package com.example.bangkit_2024_bpaai.Service.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyBackgroundService : Service() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service dijalankan...")

        serviceScope.launch {
            for (i in 1..50) {
                delay(1000)
                Log.d(TAG, "Do Something $i")
            }
            stopSelf() // untuk menghentikan atau mematikan service dari dalam kelas service itu sendiri
            Log.d(TAG, "Service dihentikan")
        }

        return START_STICKY // menandakan bila service tersebut dimatikan oleh sistem Android karena kekurangan memori
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        Log.d(TAG, "onDestroy: Service dihentikan")
    }

    companion object {
        internal val TAG = MyBackgroundService::class.java.simpleName
    }
}