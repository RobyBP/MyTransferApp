package com.robybp.mytransferapp.notificationscheduler

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.robybp.mytransferapp.util.NotificationWorker
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class ScheduleNotificationUseCase(private val context: Context) {

    fun sendNotification(dateOfArrival: String) {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = LocalDate.parse(dateOfArrival, formatter)
        val diff = ChronoUnit.DAYS.between(LocalDate.now().plusDays(2), date) * 12
        val uploadWorkRequest: WorkRequest =
            if (ChronoUnit.DAYS.between(
                    LocalDate.now(),
                    date
                ) in 2..3
            ) OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(
                12,
                TimeUnit.HOURS
            ).build()
            else OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(
                diff,
                TimeUnit.HOURS
            ).build()

        WorkManager
            .getInstance(context)
            .enqueue(uploadWorkRequest)
    }
}
