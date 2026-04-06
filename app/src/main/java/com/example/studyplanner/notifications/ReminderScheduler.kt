package com.example.studyplanner.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.studyplanner.worker.StudyReminderWorker
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    fun scheduleTestReminder(context: Context) {
        val request = OneTimeWorkRequestBuilder<StudyReminderWorker>()
            .setInitialDelay(15, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "study_reminder_test",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}