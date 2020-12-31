package com.example.todoplusminus.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.todoplusminus.R
import com.example.todoplusminus.ui.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    companion object{
        const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID : Int = 0
        const val PRIMARY_CHANNEL_ID : String = "primary_notification_channel"
    }

    lateinit var notificationManager : NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("godgod", "${intent}")
        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        deliverNotification(context)
    }

    private fun deliverNotification(context: Context){
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder =
            NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.sunflower)
                .setContentTitle("Test")
                .setContentText("test content")
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun createNotificationChannel(){

        val notificationChannel = NotificationChannel(
            PRIMARY_CHANNEL_ID,
            "stand up notification",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "aaaa"
        notificationManager.createNotificationChannel( notificationChannel)

    }
}