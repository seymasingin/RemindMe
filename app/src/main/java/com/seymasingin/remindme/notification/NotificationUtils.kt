package com.seymasingin.remindme.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.seymasingin.remindme.R


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
            .setContentTitle("Notification")
            .setContentText(reminderText)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500))

        notificationManager.notify(1, builder.build())
    }
}