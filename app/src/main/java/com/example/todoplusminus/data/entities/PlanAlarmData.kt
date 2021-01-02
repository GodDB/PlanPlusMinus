package com.example.todoplusminus.data.entities

import java.time.LocalTime

data class PlanAlarmData(
    val planId: BaseID,
    val planTitle: String?,
    val alarmId: Int,
    var alarmTime: LocalTime,
    var alarmRepeatMonday: Boolean = false,
    var alarmRepeatTuesday: Boolean = false,
    var alarmRepeatWednesday: Boolean = false,
    var alarmRepeatThursday: Boolean = false,
    var alarmRepeatFriday: Boolean = false,
    var alarmRepeatSaturday: Boolean = false,
    var alarmRepeatSunday: Boolean = false,
) {
    companion object {
        const val NONE_ALARM_ID = 0

        fun create(planId: BaseID) : PlanAlarmData {
            return PlanAlarmData(planId, null, NONE_ALARM_ID, LocalTime.now())
        }
    }

}