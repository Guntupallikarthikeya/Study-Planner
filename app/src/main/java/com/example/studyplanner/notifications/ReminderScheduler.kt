package com.example.studyplanner.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.studyplanner.worker.StudyReminderWorker
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    fun scheduleReminderAtTime(
        context: Context,
        reminderTimeMillis: Long
    ) {
        val currentTime = System.currentTimeMillis()
        val delayMillis = reminderTimeMillis - currentTime

        if (delayMillis <= 0) return

        val inputData = Data.Builder()
            .putString("title", "Study Reminder")
            .putString("message", "It is time to review your study tasks.")
            .build()

        val request = OneTimeWorkRequestBuilder<StudyReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "study_reminder_exact_time",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}