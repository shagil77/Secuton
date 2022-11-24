package com.shagil.secuton.presentation.home.todoTask

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.shagil.secuton.R
import com.shagil.secuton.presentation.home.HomeActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, receiverIntent: Intent?) {
        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val title = receiverIntent?.getStringExtra("title")
        val description = receiverIntent?.getStringExtra("description")

        val largeIcon:Bitmap = ContextCompat.getDrawable(context!!, R.drawable.app_icon)?.toBitmap()!!

        val builder = NotificationCompat.Builder(context, "secutonAlarmNotification")
            .setLargeIcon(largeIcon)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123, builder.build())
    }
}