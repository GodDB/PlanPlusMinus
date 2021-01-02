package com.example.todoplusminus.ui.main.history.alarm

import android.util.EventLog
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.todoplusminus.util.livedata.Event
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

class PlanHistoryAlarmVM() {

    private val alarmTime : MutableLiveData<LocalTime> = MutableLiveData(LocalTime.of(1,11))

    val alarmHour : MutableLiveData<Int> = MediatorLiveData<Int>().apply {
        addSource(alarmTime){
            this.value = it.hour
        }
    }

    val alarmMinute : MutableLiveData<Int> = MediatorLiveData<Int>().apply {
        addSource(alarmTime){
            this.value = it.minute
        }
    }

    val repeatAlarmToMonday : MutableLiveData<Boolean> = MutableLiveData()
    val repeatAlarmToTuesday : MutableLiveData<Boolean> = MutableLiveData()
    val repeatAlarmToWednesday : MutableLiveData<Boolean> = MutableLiveData()
    val repeatAlarmToThursday : MutableLiveData<Boolean> = MutableLiveData()
    val repeatAlarmToFriday : MutableLiveData<Boolean> = MutableLiveData()
    val repeatAlarmToSaturday : MutableLiveData<Boolean> = MutableLiveData()
    val repeatAlarmToSunday : MutableLiveData<Boolean> = MutableLiveData()

    val closeEditor : MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))

    fun onDone(){
        Log.d("godgod", "${alarmHour.value}    ${alarmMinute.value}")
    }

    fun onClose(){
        closeEditor.value = Event(true)
    }

    fun checkRepeatDay(tag : DayOfWeek){
        when(tag){
            DayOfWeek.MONDAY ->
                repeatAlarmToMonday.value = !confirmToDuplicateCheck(repeatAlarmToMonday.value)
            DayOfWeek.TUESDAY ->
                repeatAlarmToTuesday.value = !confirmToDuplicateCheck(repeatAlarmToTuesday.value)
            DayOfWeek.WEDNESDAY ->
                repeatAlarmToWednesday.value = !confirmToDuplicateCheck(repeatAlarmToWednesday.value)
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
    private fun confirmToDuplicateCheck(value : Boolean?) : Boolean {
        if(value == null) return false
        return value
    }
}