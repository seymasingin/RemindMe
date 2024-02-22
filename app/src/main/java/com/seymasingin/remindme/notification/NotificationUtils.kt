package com.seymasingin.remindme.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService


object NotificationUtils {

    fun showNotification(context: Context, reminderText: String) {
        val builder: NotificationCompat.Builder

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "CHANNEL_ID"
        val channelName = "CHANNEL_NAME"

        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(channel)

        builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle("İleri Kotlin - Work Manager")
            .setContentText(reminderText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(1, builder.build())
    }
}