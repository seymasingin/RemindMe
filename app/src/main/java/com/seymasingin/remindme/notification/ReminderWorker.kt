package com.seymasingin.remindme.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val reminderText = workerParams.inputData.getString("reminderText").orEmpty()
        NotificationUtils.showNotification(context, reminderText)
        return Result.success()
    }
}