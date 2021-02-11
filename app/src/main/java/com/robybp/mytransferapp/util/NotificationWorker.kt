package com.robybp.mytransferapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.robybp.mytransferapp.R

class NotificationWorker(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val builder = NotificationCompat.Builder(appContext, "123")
            .setSmallIcon(R.drawable.hrv)
            .setContentInfo("My Transfer App")
            .setContentText(appContext.resources.getString(R.string.notification_description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        createNotificationChannel()
        with(NotificationManagerCompat.from(appContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(1234, builder.build())
        }
        return Result.success()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Test Channel"
            val descriptionText = "Test description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("123", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
