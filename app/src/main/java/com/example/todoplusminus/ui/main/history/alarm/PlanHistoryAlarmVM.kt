package com.example.todoplusminus.ui.main.history.alarm

import android.util.EventLog
import android.util.Log
import androidx.lifecycle.*
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.data.entities.PlanAlarmData
import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.util.livedata.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

class PlanHistoryAlarmVM @Inject constructor(
    private val targetId: BaseID,
    private val alarmId: Int,
    private val repo: IPlannerRepository
) {

    private val alarmData: LiveData<PlanAlarmData> =
        if (alarmId != PlanAlarmData.NONE_ALARM_ID) repo.getAlarmData(alarmId).asLiveData()
        else MutableLiveData(PlanAlarmData.create(targetId))

    private val alarmTime: LiveData<LocalTime> = alarmData.switchMap { alarmData ->
        MutableLiveData(alarmData.alarmTime)
    }

    val alarmHour: MutableLiveData<Int> = MediatorLiveData<Int>().apply {
        addSource(alarmTime) {
            this.value = it.hour
        }
    }

    val alarmMinute: MutableLiveData<Int> = MediatorLiveData<Int>().apply {
        addSource(alarmTime) {
            this.value = it.minute
        }
    }

    val repeatAlarmToMonday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it.alarmRepeatMonday
        }
    }
    val repeatAlarmToTuesday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it.alarmRepeatTuesday
        }
    }
    val repeatAlarmToWednesday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it.alarmRepeatWednesday
        }
    }
    val repeatAlarmToThursday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it.alarmRepeatThursday
        }
    }
    val repeatAlarmToFriday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it.alarmRepeatFriday
        }
    }
    val repeatAlarmToSaturday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it.alarmRepeatSaturday
        }
    }
    val repeatAlarmToSunday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it.alarmRepeatSunday
        }
    }

    val closeEditor: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))

    //todo 알람을 제대로 설정하지 않았을때 done 버튼 막기
    fun onDone() {
        val alarmData = alarmData.value ?: PlanAlarmData.create(targetId)
        val newAlarmData = reflectChangeValueTo(alarmData)

        CoroutineScope(Dispatchers.Main).launch {
            repo.insertAlarmData(newAlarmData)
            onClose()
        }
    }

    fun onClose() {
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

    private fun reflectChangeValueTo(alarmData: PlanAlarmData) : PlanAlarmData{
        //null일 수가 없다... 강제 캐팅해도 괜찮다
        val newTime = LocalTime.of(alarmHour.value!!, alarmMinute.value!!)

        return alarmData.apply {
            this.alarmTime = newTime
            this.alarmRepeatMonday = repeatAlarmToMonday.value ?: false
            this.alarmRepeatTuesday = repeatAlarmToTuesday.value ?: false
            this.alarmRepeatWednesday = repeatAlarmToWednesday.value ?: false
            this.alarmRepeatThursday = repeatAlarmToThursday.value ?: false
            this.alarmRepeatFriday = repeatAlarmToFriday.value ?: false
            this.alarmRepeatSaturday = repeatAlarmToSaturday.value ?: false
            this.alarmRepeatSunday = repeatAlarmToSunday.value ?: false
        }
    }

    //두번 눌렀을 경우엔 check해제를 해야하므로 중복 체크인지를 확인한다.
    private fun confirmToDuplicateCheck(value: Boolean?): Boolean {
        if (value == null) return false
        return value
    }
}