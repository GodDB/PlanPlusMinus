package com.example.todoplusminus.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat.getSystemService
import com.example.todoplusminus.data.entities.PlanAlarmData
import java.time.*
import java.time.temporal.TemporalAdjusters

class AlarmManagerHelper(private val context: Context) {

    companion object {
        const val TITLE_ID: String = "title_id"

        private val ALARM_RECEIVER_CLASS: Class<AlarmReceiver> = AlarmReceiver::class.java

        //todo test
   /*     private const val INTERVAL_TIME : Long = AlarmManager.INTERVAL_DAY * 7*/
        private const val INTERVAL_TIME : Long = 1000 * 60 * 3

        const val TYPE_ONESHOT = 1
        const val TYPE_REPEAT = 2
    }

    data class AlarmItem(
        val alarmItemId: Int,
        val alarmItemDate: LocalDateTime,
    )

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun registerAlarm(alarmData: PlanAlarmData, alarmType: Int) {
        val alarmItemList = convertAlarmDataToAlarmItemList(alarmData)

        Log.d("godgod", "${alarmItemList}")
        alarmItemList.forEach {
            registerAlarm(it.alarmItemId, alarmData.planTitle, it.alarmItemDate, alarmType)
        }
    }

    private fun registerAlarm(
        alarmId: Int,
        alarmTitle: String?,
        time: LocalDateTime,
        alarmType: Int
    ) {
        when(alarmType){
            TYPE_ONESHOT -> registerAlarmOneShot(alarmId, alarmTitle, time)
            TYPE_REPEAT -> registerAlarmRepeat(alarmId, alarmTitle, time)
        }

    }

    private fun registerAlarmRepeat(alarmId: Int, alarmTitle : String?, time: LocalDateTime){
        //리시버에 전달
        val intent = Intent(context, ALARM_RECEIVER_CLASS)
        intent.putExtra(TITLE_ID, alarmTitle)

        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val triggerTime = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            INTERVAL_TIME,
            pendingIntent
        )
    }

    private fun registerAlarmOneShot(alarmId: Int, alarmTitle: String?, time : LocalDateTime){

    }

    fun cancelAlarm(alarmDate: PlanAlarmData) {
        val alarmItemList = convertAlarmDataToAlarmItemList(alarmDate)

        Log.d("godgod", "$alarmItemList")
        alarmItemList.forEach {
            cancelAlarm(it.alarmItemId)
        }
    }

    private fun cancelAlarm(alarmId: Int) {
        val intent = Intent(context, ALARM_RECEIVER_CLASS)

        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
    }

    private fun convertAlarmDataToAlarmItemList(alarmData: PlanAlarmData) : List<AlarmItem> {
        val alarmItems = mutableListOf<AlarmItem>()

        if (alarmData.alarmRepeatMonday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 1),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 1)
            )
        )
        if (alarmData.alarmRepeatTuesday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 2),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 2)
            )
        )
        if (alarmData.alarmRepeatWednesday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 3),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 3)
            )
        )
        if (alarmData.alarmRepeatThursday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 4),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 4)
            )
        )
        if (alarmData.alarmRepeatFriday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 5),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 5)
            )
        )
        if (alarmData.alarmRepeatSaturday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 6),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 6)
            )
        )
        if (alarmData.alarmRepeatSunday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 7),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 7)
            )
        )

        return alarmItems
    }

   fun generateAlarmItemId(alarmId: Int, dayOfWeekValue: Int): Int {
        return (alarmId.toString() + dayOfWeekValue.toString()).toInt()
    }


   private fun generateAlarmItemDateTime(alarmTime: LocalTime, dayOfWeekValue: Int): LocalDateTime {
        return LocalDateTime.now()
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeekValue)))
            .with(alarmTime)
    }
}

