package com.example.todoplusminus.ui.main.history.alarm

import android.util.EventLog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.todoplusminus.data.entities.PlanAlarmData
import com.example.todoplusminus.util.livedata.Event
import java.time.DayOfWeek
import java.time.LocalTime

/**
 * alarm vm의 기초
 *
 * 구현객체는 모드(create , update)에 따라 분리된다.
 * */
interface BaseHistoryAlarmVM {

    val alarmData : LiveData<PlanAlarmData?>

    val alarmTime : LiveData<LocalTime>

    val alarmHour : MutableLiveData<Int>

    val alarmMinute : MutableLiveData<Int>

    val repeatAlarmToMonday : MutableLiveData<Boolean>

    val repeatAlarmToTuesday : MutableLiveData<Boolean>

    val repeatAlarmToWednesday : MutableLiveData<Boolean>

    val repeatAlarmToThursday : MutableLiveData<Boolean>

    val repeatAlarmToFriday : MutableLiveData<Boolean>

    val repeatAlarmToSaturday : MutableLiveData<Boolean>

    val repeatAlarmToSunday : MutableLiveData<Boolean>

    val isDoneAllow : LiveData<Boolean>

    val showDeleteBtn : LiveData<Boolean>

    val closeEditor : MutableLiveData<Event<Boolean>>

    fun onDone()

    fun onDelete(alarmId : Int) {}

    fun onClose(){
        closeEditor.value = Event(true)
    }

    fun checkRepeatDay(tag: DayOfWeek) {
        when (tag) {
            DayOfWeek.MONDAY ->
                repeatAlarmToMonday.value = !confirmToDuplicateCheck(repeatAlarmToMonday.value)
            DayOfWeek.TUESDAY ->
                repeatAlarmToTuesday.value = !confirmToDuplicateCheck(repeatAlarmToTuesday.value)
            DayOfWeek.WEDNESDAY ->
                repeatAlarmToWednesday.value =
                    !confirmToDuplicateCheck(repeatAlarmToWednesday.value)
            DayOfWeek.THURSDAY ->
                repeatAlarmToThursday.value = !confirmToDuplicateCheck(repeatAlarmToThursday.value)
            DayOfWeek.FRIDAY ->
                repeatAlarmToFriday.value = !confirmToDuplicateCheck(repeatAlarmToFriday.value)
            DayOfWeek.SATURDAY ->
                repeatAlarmToSaturday.value = !confirmToDuplicateCheck(repeatAlarmToSaturday.value)
            DayOfWeek.SUNDAY ->
                repeatAlarmToSunday.value = !confirmToDuplicateCheck(repeatAlarmToSunday.value)
        }
    }

    //두번 눌렀을 경우엔 check해제를 해야하므로 중복 체크인지를 확인한다.
    private fun confirmToDuplicateCheck(value: Boolean?): Boolean {
        if (value == null) return false
        return value
    }

}