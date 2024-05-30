package com.example.bangkit_2024_bpaai.Firebase.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.bangkit_2024_bpaai.Firebase.MainFirebase
import com.example.bangkit_2024_bpaai.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    // untuk mengetahui token dari device yang digunakan
    override fun onNewToken(token: String) {
        // Token berfungsi sebagai id penerima ketika notification dikirimkan dari server
        Log.d(TAG, "Refreshed token: $token")
    }

    // untuk menerima push notification dan menampilkannya
    // RemoteMessage berisi berbagai macam data seperti id pengirim, data dari additional options serta data notifikasi seperti judul dan body
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "Message data payload: " + remoteMessage.data)
        Log.d(TAG, "Message Notification Body: ${remoteMessage.notification?.body}")

        sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val contentIntent = Intent(applicationContext, MainFirebase::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    companion object {
        private val TAG = MyFirebaseMessagingService::class.java.simpleName
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_ID = "Firebase Channel"
        private const val NOTIFICATION_CHANNEL_NAME = "Firebase Notification"
    }
}