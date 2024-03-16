package com.seymasingin.remindme.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.seymasingin.remindme.MainActivity
import com.seymasingin.remindme.R

object NotificationUtils {

    fun showNotification(context: Context, reminderText: String) {
        val builder: NotificationCompat.Builder

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)

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
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500))

        notificationManager.notify(1, builder.build())
    }
}