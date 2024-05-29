package com.teamdontbe.dontbe.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.util.FcmTag.RELATED_CONTENT_ID
import timber.log.Timber

class DontBeFirebaseMessagingService : FirebaseMessagingService() {

    private val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("fcm token : $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        sendPushAlarm(message.data)
    }

    private fun sendPushAlarm(data: MutableMap<String, String>) {
        createPushAlarmChannel()

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(RELATED_CONTENT_ID, data[RELATED_CONTENT_ID])
            },
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(com.teamdontbe.feature.R.drawable.ic_login_symbol)
            .setContentTitle(data[TITLE])
            .setContentText(data[BODY]).setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun createPushAlarmChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_NAME = "FCM_CHANNEL"
        private const val CHANNEL_ID = "FCM_CHANNEL_ID"
        private const val TITLE = "title"
        private const val BODY = "body"
    }
}
