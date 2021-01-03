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

    private val alarmData: LiveData<PlanAlarmData?> =
        if (alarmId != PlanAlarmData.NONE_ALARM_ID) repo.getAlarmData(alarmId).asLiveData()
        else MutableLiveData(PlanAlarmData.create(targetId))

    private val alarmTime: LiveData<LocalTime> = alarmData.switchMap { alarmData ->
        MutableLiveData(alarmData?.alarmTime ?: LocalTime.now())
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
            this.value = it?.alarmRepeatMonday
        }
    }
    val repeatAlarmToTuesday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatTuesday
        }
    }
    val repeatAlarmToWednesday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatWednesday
        }
    }
    val repeatAlarmToThursday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatThursday
        }
    }
    val repeatAlarmToFriday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatFriday
        }
    }
    val repeatAlarmToSaturday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatSaturday
        }
    }
    val repeatAlarmToSunday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatSunday
        }
    }

    /**
     * done 버튼 액션을 제어하는 livedata
     *
     * 사용자는 월~일요일까지 중 하나 이상을 선택해야만 done버튼을 사용할 수 있다.
     * */
    val isDoneAllow : LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(repeatAlarmToMonday){
            this.value = checkRepeatDay()
        }
        addSource(repeatAlarmToTuesday){
            this.value = checkRepeatDay()
        }
        addSource(repeatAlarmToWednesday){
            this.value = checkRepeatDay()
        }
        addSource(repeatAlarmToThursday){
            this.value = checkRepeatDay()
        }
        addSource(repeatAlarmToFriday){
            this.value = checkRepeatDay()
        }
        addSource(repeatAlarmToSaturday){
            this.value = checkRepeatDay()
        }
        addSource(repeatAlarmToSunday){
            this.value = checkRepeatDay()
        }
    }

    val showDeleteBtn : LiveData<Boolean> = MutableLiveData(alarmId != PlanAlarmData.NONE_ALARM_ID)

    val closeEditor: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))


    fun onDone() {
        if(!checkRepeatDay()) return

        val alarmData = alarmData.value ?: PlanAlarmData.create(targetId)
        val newAlarmData = reflectChangeValueTo(alarmData)
        CoroutineScope(Dispatchers.Main).launch {
            repo.insertAlarmData(newAlarmData)
            onClose()
        }
    }

    fun onDelete(alarmId : Int){
        CoroutineScope(Dispatchers.Main).launch {
            onClose()
            repo.deleteAlarmDataById(alarmId)
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
        //null일 수가 없다... 강제 캐스팅해도 괜찮다
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

    //repeatDay들의 값이 모두 false인지를 확인한다
    private fun checkRepeatDay() : Boolean{
        var result : Boolean = false

        if(this.repeatAlarmToMonday.value == true) result = true
        if(this.repeatAlarmToTuesday.value == true) result = true
        if(this.repeatAlarmToWednesday.value == true) result = true
        if(this.repeatAlarmToThursday.value == true) result = true
        if(this.repeatAlarmToFriday.value == true) result = true
        if(this.repeatAlarmToSaturday.value == true) result = true
        if(this.repeatAlarmToSunday.value == true) result = true

        return result
    }
}