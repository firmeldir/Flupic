package com.example.flupic.scrobbler.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.flupic.scrobbler.service.MusicSharingService


internal object NotificationUtil {

    private const val CHANNEL_ID = "scrobbler_channel"


    const val NL_SERVICE_NOTIFICATION_ID = 22233311

    const val EXTRA_STARTED_FROM_NOTIFICATION = "$PACKAGE_NAME.started_from_notification"


    fun create(service: MusicSharingService) : Notification{

        createNotificationChannel(service)

        val intent = Intent(service, MusicSharingService::class.java).apply {
            putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)
        }

        val servicePendingIntent = PendingIntent.getService(service.applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(service.applicationContext,
            CHANNEL_ID
        )
            .addAction(
                com.example.flupic.R.drawable.ic_cancel, service.applicationContext.getString(com.example.flupic.R.string.notification_stop),
                servicePendingIntent)

            .setContentTitle(service.applicationContext.getString(com.example.flupic.R.string.nl_service_notification_title))
            .setContentText(service.applicationContext.getString(com.example.flupic.R.string.nl_service_notification_sub))
            .setOngoing(true)
            .setPriority(Notification.PRIORITY_HIGH)
            .setSmallIcon(com.example.flupic.R.drawable.ic_muzzly_backlogo)
            .setLargeIcon(service.getDrawable(com.example.flupic.R.drawable.ic_muzzly_backlogo)?.toBitmap(160,160))
            .setWhen(System.currentTimeMillis())

        return builder.build()
    }

    private fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notifyManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                context.getString(com.example.flupic.R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH)

            notifyManager.createNotificationChannel(notificationChannel)
        }
    }
}