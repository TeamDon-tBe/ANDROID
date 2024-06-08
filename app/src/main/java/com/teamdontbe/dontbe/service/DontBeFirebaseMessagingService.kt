package com.teamdontbe.dontbe.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.Constants.MessageNotificationKeys.ENABLE_NOTIFICATION
import com.google.firebase.messaging.Constants.MessageNotificationKeys.NOTIFICATION_PREFIX
import com.google.firebase.messaging.Constants.MessageNotificationKeys.NOTIFICATION_PREFIX_OLD
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.util.FcmTag
import com.teamdontbe.feature.util.FcmTag.CHANNEL_ID
import com.teamdontbe.feature.util.FcmTag.NOTIFICATION_BODY
import com.teamdontbe.feature.util.FcmTag.NOTIFICATION_ID
import com.teamdontbe.feature.util.FcmTag.NOTIFICATION_TITLE
import com.teamdontbe.feature.util.FcmTag.RELATED_CONTENT_ID
import timber.log.Timber

class DontBeFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var title: String
    private lateinit var body: String

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("fcm new token : $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        sendPushAlarm(
            title = if (::title.isInitialized) title else "",
            body = if (::body.isInitialized) body else "",
            contentId = message.data[RELATED_CONTENT_ID] ?: "-1"
        )
    }

    private fun sendPushAlarm(title: String, body: String, contentId: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        createNotificationChannel(notificationManager)
        val notification = buildNotification(title, body, contentId)
        notificationManager?.notify(NOTIFICATION_ID, notification)
    }

    override fun handleIntent(intent: Intent?) {
        val newPushAlarmIntent = intent?.apply {
            val temp = extras?.apply {
                title = getString(NOTIFICATION_TITLE).orEmpty()
                body = getString(NOTIFICATION_BODY).orEmpty()
                remove(ENABLE_NOTIFICATION)
                remove(getKeyWithOldPrefix())
            }
            replaceExtras(temp)
        }
        super.handleIntent(newPushAlarmIntent)
    }

    private fun getKeyWithOldPrefix(): String {
        val key = ENABLE_NOTIFICATION
        return if (!key.startsWith(NOTIFICATION_PREFIX)) {
            key
        } else key.replace(
            NOTIFICATION_PREFIX,
            NOTIFICATION_PREFIX_OLD
        )
    }

    private fun buildNotification(
        title: String,
        body: String,
        contentId: String
    ): Notification {
        val pendingIntent = createPendingIntent(contentId)
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(com.teamdontbe.feature.R.drawable.ic_login_symbol)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setShowWhen(true)
            .build()
    }

    private fun createPendingIntent(contentId: String): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(RELATED_CONTENT_ID, contentId)
        }
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    private fun createNotificationChannel(notificationManager: NotificationManager?) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            FcmTag.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager?.createNotificationChannel(channel)
    }
}
