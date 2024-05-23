package com.teamdontbe.dontbe.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class DontBeFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("fcm token : $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_channel"

        val channelName = "Default Channel"
        val channelDescription = "This is the default notification channel."
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        notificationManager.createNotificationChannel(channel)

        val title = message.data["title"]
        val body = message.data["data"]
        val contentId = message.data["relatedContentId"]
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(com.teamdontbe.feature.R.drawable.ic_appbar_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(1, notificationBuilder.build())
    }

    companion object {
        val PUSH_ALARM_CHANNEL = listOf(1, 2, 3)
    }
}
