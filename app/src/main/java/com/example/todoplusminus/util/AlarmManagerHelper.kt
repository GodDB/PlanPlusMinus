package com.example.todoplusminus.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.todoplusminus.data.entities.PlanAlarmData
import com.example.todoplusminus.receivers.AlarmReceiver
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

class AlarmManagerHelper(private val context: Context) {

    companion object {
        //alarm receiver에게 Intent로 전달할 알람 내용 id
        const val ALARM_CONTENT_ID: String = "content_id"

        //default 알람용 (매일 오후 10시 알람)
        private const val DEFAULT_ALARM_ID : Int = 0
        private const val DEFALUT_ALARM_INTERVAL_TIME = AlarmManager.INTERVAL_DAY

        private val ALARM_RECEIVER_CLASS: Class<AlarmReceiver> = AlarmReceiver::class.java

        //일반 알람용 (사용자 선택)
        private const val INTERVAL_TIME : Long = AlarmManager.INTERVAL_DAY * 7

        const val TYPE_ONESHOT = 1
        const val TYPE_REPEAT = 2
    }

    data class AlarmItem(
        val alarmItemId: Int,
        val alarmItemDate: LocalDateTime,
        val alarmItemContent : String?
    )

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    //default 알람 등록 (매일 오후 10시 알람)
    fun registerDefaultAlarm(){
        if(checkAlreadyRegistAlarm(DEFAULT_ALARM_ID)) return

        val defaultAlarmContent = "수고했어, 오늘도\uD83D\uDE0A"+
                "\n " +
                "당신의 하루를 응원합니다.\uD83D\uDE4C"
        val defaultAlarmTime = LocalDateTime.now()
            .with( LocalTime.of(22, 0) )

        registerAlarmRepeat(DEFAULT_ALARM_ID, defaultAlarmContent, defaultAlarmTime, DEFALUT_ALARM_INTERVAL_TIME)
    }

    fun unregisterDefaultAlarm(){
        unregisterAlarm(DEFAULT_ALARM_ID)
    }

    //일반 알람 등
    fun registerAlarm(alarmData: PlanAlarmData, alarmType: Int) {
        val alarmItemList = convertAlarmDataToAlarmItemList(alarmData)

        Log.d("godgod", "${alarmItemList}")
        alarmItemList.forEach {
            registerAlarm(it.alarmItemId, it.alarmItemContent, it.alarmItemDate, alarmType)
        }
    }

    private fun registerAlarm(
        alarmId: Int,
        alarmContent: String?,
        time: LocalDateTime,
        alarmType: Int
    ) {
        when(alarmType){
            TYPE_ONESHOT -> registerAlarmOneShot(alarmId, alarmContent, time)
            TYPE_REPEAT -> registerAlarmRepeat(alarmId, alarmContent, time, INTERVAL_TIME)
        }

    }

    private fun registerAlarmRepeat(alarmId: Int, alarmContent : String?, time: LocalDateTime, intervalTime : Long){
        //리시버에 전달
        val intent = Intent(context, ALARM_RECEIVER_CLASS)
        intent.putExtra(ALARM_CONTENT_ID, alarmContent)

        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val triggerTime = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            intervalTime,
            pendingIntent
        )

        Log.d("godgod", "알람 등록 id :${alarmId}    title : ${alarmContent}")
    }

    private fun registerAlarmOneShot(alarmId: Int, alarmContent : String?, time : LocalDateTime){

    }

    fun unregisterAlarm(alarmDate: PlanAlarmData) {
        val alarmItemList = convertAlarmDataToAlarmItemList(alarmDate)

        Log.d("godgod", "$alarmItemList")
        alarmItemList.forEach {
            unregisterAlarm(it.alarmItemId)
        }
    }

    private fun unregisterAlarm(alarmId: Int) {
        val intent = Intent(context, ALARM_RECEIVER_CLASS)

        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()

        Log.d("godgod", "알람 해제 id :${alarmId}")
    }

    private fun convertAlarmDataToAlarmItemList(alarmData: PlanAlarmData) : List<AlarmItem> {
        val alarmItems = mutableListOf<AlarmItem>()

        if (alarmData.alarmRepeatMonday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 1),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 1),
                alarmItemContent = alarmData.planTitle
            )
        )
        if (alarmData.alarmRepeatTuesday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 2),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 2),
                alarmItemContent = alarmData.planTitle
            )
        )
        if (alarmData.alarmRepeatWednesday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 3),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 3),
                alarmItemContent = alarmData.planTitle
            )
        )
        if (alarmData.alarmRepeatThursday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 4),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 4),
                alarmItemContent = alarmData.planTitle
            )
        )
        if (alarmData.alarmRepeatFriday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 5),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 5),
                alarmItemContent = alarmData.planTitle
            )
        )
        if (alarmData.alarmRepeatSaturday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 6),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 6),
                alarmItemContent = alarmData.planTitle
            )
        )
        if (alarmData.alarmRepeatSunday) alarmItems.add(
            AlarmItem(
                alarmItemId = generateAlarmItemId(alarmData.alarmId, 7),
                alarmItemDate = generateAlarmItemDateTime(alarmData.alarmTime, 7),
                alarmItemContent = alarmData.planTitle
            )
        )

        return alarmItems
    }

   private fun generateAlarmItemId(alarmId: Int, dayOfWeekValue: Int): Int {
        return (alarmId.toString() + dayOfWeekValue.toString()).toInt()
    }


   private fun generateAlarmItemDateTime(alarmTime: LocalTime, dayOfWeekValue: Int): LocalDateTime {
        return LocalDateTime.now()
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeekValue)))
            .with(alarmTime)
    }


    //동일한 알림id로 알람이 등록되어 있는지 확인한다.
    // true면 등록되어 있음, false면 등록되어 있지 않다.
    private fun checkAlreadyRegistAlarm(alarmId: Int) : Boolean{

        val intent = Intent(context, ALARM_RECEIVER_CLASS)

        return PendingIntent.getBroadcast(
            context, alarmId,
            intent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }
}

