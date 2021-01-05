package com.example.todoplusminus.ui.main.history.alarm

import android.util.Log
import androidx.lifecycle.*
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.data.entities.PlanAlarmData
import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.util.AlarmManagerHelper
import com.example.todoplusminus.util.livedata.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import java.time.LocalTime

class PlanHistoryAlarmCreateMode(
    private val targetId : BaseID,
    private val repo : IPlannerRepository
) : BaseHistoryAlarmVM {

    init {
        CoroutineScope(Dispatchers.Main).launch {
            mNewAlarmId = ( repo.getLatestAlarmId().first() ?: 0 ) + 1
            mAlarmTitle = repo.getPlannerTitle(targetId).first()
        }
    }

    private var mNewAlarmId : Int = 0

    private var mAlarmTitle : String = ""

    override val alarmData: LiveData<PlanAlarmData?> = MutableLiveData(PlanAlarmData.create(targetId))

    override val alarmTime: LiveData<LocalTime> = alarmData.switchMap { alarmData ->
        MutableLiveData(alarmData?.alarmTime ?: LocalTime.now())
    }
    override val alarmHour: MutableLiveData<Int> = MediatorLiveData<Int>().apply {
        addSource(alarmTime) {
            this.value = it.hour
        }
    }
    override val alarmMinute: MutableLiveData<Int> = MediatorLiveData<Int>().apply {
        addSource(alarmTime) {
            this.value = it.minute
        }
    }
    override val repeatAlarmToMonday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatMonday
        }
    }
    override val repeatAlarmToTuesday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatTuesday
        }
    }
    override val repeatAlarmToWednesday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatWednesday
        }
    }

    override val repeatAlarmToThursday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatThursday
        }
    }
    override val repeatAlarmToFriday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatFriday
        }
    }
    override val repeatAlarmToSaturday: MutableLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatSaturday
        }
    }
    override val repeatAlarmToSunday: MutableLiveData<Boolean>  = MediatorLiveData<Boolean>().apply {
        addSource(alarmData){
            this.value = it?.alarmRepeatSunday
        }
    }
    override val isDoneAllow: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
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

    override val showDeleteBtn: LiveData<Boolean> = MutableLiveData(false)

    override val closeEditor: MutableLiveData<Event<Boolean>> = MutableLiveData()

    override fun onDone() {
        if(!checkRepeatDay()) return

        val alarmData = alarmData.value ?: PlanAlarmData.create(targetId)
        val newAlarmData = reflectChangeValueTo(alarmData)

        CoroutineScope(Dispatchers.Main).launch {
            repo.insertAlarmData(newAlarmData)
            onClose()
        }
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

    private fun reflectChangeValueTo(alarmData: PlanAlarmData) : PlanAlarmData{
        //null일 수가 없다... 강제 캐스팅해도 괜찮다
        val newTime = LocalTime.of(alarmHour.value!!, alarmMinute.value!!)
        val newAlarmId = mNewAlarmId

        return alarmData.apply {
            this.alarmId = newAlarmId
            this.alarmTime = newTime
            this.planTitle = mAlarmTitle
            this.alarmRepeatMonday = repeatAlarmToMonday.value ?: false
            this.alarmRepeatTuesday = repeatAlarmToTuesday.value ?: false
            this.alarmRepeatWednesday = repeatAlarmToWednesday.value ?: false
            this.alarmRepeatThursday = repeatAlarmToThursday.value ?: false
            this.alarmRepeatFriday = repeatAlarmToFriday.value ?: false
            this.alarmRepeatSaturday = repeatAlarmToSaturday.value ?: false
            this.alarmRepeatSunday = repeatAlarmToSunday.value ?: false
        }
    }

    private fun generateRequestCode( alarmId : Int, dayOfWeek : Int) : Int{
        val str = alarmId.toString() + dayOfWeek.toString()
        return str.toInt()
    }

}