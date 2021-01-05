package com.example.todoplusminus.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.PowerManager
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
    lateinit var powerManager : PowerManager

    override fun onReceive(context: Context, intent: Intent) {
       val title = intent.getStringExtra(AlarmManagerHelper.TITLE_ID)
        Log.d("godgod", "$title")

        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager

        powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        createNotificationChannel()
        deliverNotification(context, title ?: "")
    }

    private fun deliverNotification(context: Context, title : String){
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
                .setContentTitle(title)
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        val wakeLock : PowerManager.WakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MyApp::MyWakelockTag")
        wakeLock.acquire(10*60*1000L /*10 minutes*/)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
        wakeLock.release()
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
        notificationChannel.description = "stand up notification"
        notificationManager.createNotificationChannel( notificationChannel)

    }
}