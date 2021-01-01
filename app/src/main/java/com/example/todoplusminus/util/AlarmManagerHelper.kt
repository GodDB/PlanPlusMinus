package com.example.todoplusminus.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.content.ContextCompat.getSystemService
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class AlarmManagerHelper(private val context: Context) {

    companion object{
        private val ALARM_RECEIVER_CLASS : Class<AlarmReceiver> = AlarmReceiver::class.java

        const val TITLE_ID : String = "title_id"
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun registerAlarm(alarmId : Int, alarmTitle: String, time : LocalDateTime, alarmType : Int){

        //리시버에 전달
        val intent = Intent(context, ALARM_RECEIVER_CLASS)
        intent.putExtra(TITLE_ID, alarmTitle)

        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val zoneDate = time.atZone(ZoneId.systemDefault())
        val triggerTime = zoneDate.toInstant().toEpochMilli()
       /* val triggerTime = SystemClock.elapsedRealtime()
*/
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }

    fun cancelAlarm(alarmId: Int){
        val intent = Intent(context, ALARM_RECEIVER_CLASS)

        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
    }


}