package developer.mihailzharkovskiy.stepcounter.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.MainThread
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import developer.mihailzharkovskiy.stepcounter.R
import developer.mihailzharkovskiy.stepcounter.ui.screens.activity_main.MainActivity
import javax.inject.Inject

class StepCounterNotification @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val notificationManager: NotificationManager,
) {

//    private val notificationManager =
//        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
            .setAutoCancel(false)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentTitle(context.getString(R.string.notification_shapka))
            .setContentText(context.getString(R.string.notification_ticker, 0, 0.0))
            .setWhen(System.currentTimeMillis())
            .setDefaults(0)
            .setVibrate(longArrayOf(0))
            .setSound(null)
            .setProgress(100, 0, false)
            .setSmallIcon(R.drawable.ic_step_light)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(openActivityFromNotification())
    }

    private fun openActivityFromNotification(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).also {
            it.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return PendingIntent.getActivity(context, 0, intent, FLAG_UPDATE_CURRENT)
    }

    fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIF_CHANNEL_ID,
                NOTIF_CHANNEL_NAME,
                IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        return notificationBuilder.build()
    }

    @MainThread
    fun updateNotification(steps: Int, distance: Double, dayStepPlane: Int) {
        notificationManager.notify(
            NOTIF_ID,
            notificationBuilder
                .setContentText(context.getString(R.string.notification_ticker, steps, distance))
                .setProgress(dayStepPlane, steps, false)
                .build()
        )
    }

    companion object {
        const val NOTIF_ID = 1
        const val NOTIF_CHANNEL_ID = "CHANNEL_ID_NORIFICATION_SHAGOMER"
        const val NOTIF_CHANNEL_NAME = "CHANNEL_NAME_STEPS"
    }
}