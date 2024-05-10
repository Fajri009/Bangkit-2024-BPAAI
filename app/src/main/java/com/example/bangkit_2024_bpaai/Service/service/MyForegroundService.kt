package com.example.bangkit_2024_bpaai.Service.service

import android.app.*
import android.app.Notification.FOREGROUND_SERVICE_IMMEDIATE
import android.content.*
import android.content.pm.ServiceInfo
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.bangkit_2024_bpaai.R
import com.example.bangkit_2024_bpaai.Service.MainService
import kotlinx.coroutines.*

// untuk menjalankan service, meskipun aplikasi sudah tidak berjalan (kill dari sistem)
class MyForegroundService : Service() {
    private val serviceJob = Job()

    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notification = buildNotification()

//        startForeground(NOTIFICATION_ID, notification)
        // notifikasi di sini dibutuhkan agar pengguna tahu bahwa saat ini masih ada service yang berjalan
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        Log.d(TAG, "Service dijalankan...")
        serviceScope.launch {
            for (i in 1..50) {
                delay(1000)
                Log.d(TAG, "Do Something $i")
            }

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_DETACH)
            } else{
                stopForeground(true)
            }

            stopSelf()
            Log.d(TAG, "Service dihentikan")
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        Log.d(TAG, "onDestroy: Service dihentikan")
    }

    // membangun sebuah notifikasi yang nantinya digunakan untuk kebutuhan menjalankan foreground service
    private fun buildNotification(): Notification {
        val notificationIntent = Intent(this, MainService::class.java)
        val pendingFlags: Int = if (Build.VERSION.SDK_INT >= 23) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, pendingFlags)

        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Saat ini foreground service sedang berjalan.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            notificationBuilder.setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_NAME
            notificationBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        return notificationBuilder.build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "dicoding channel"
        internal val TAG = MyForegroundService::class.java.simpleName
    }
}
