package com.example.todoplusminus.ui.main.history.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.todoplusminus.base.BaseApplication
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.databinding.ControllerPlanHistoryAlarmBinding
import java.time.DayOfWeek
import javax.inject.Inject
import kotlin.properties.Delegates

class PlanHistoryAlarmController : DBControllerBase {

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(targetId: BaseID, alarmId : Int) {
        this._targetId = targetId
        this._alarmId = alarmId
    }

    @Inject
    lateinit var alarmVM: PlanHistoryAlarmVM
    private lateinit var binder: ControllerPlanHistoryAlarmBinding
    private lateinit var _targetId: BaseID
    private var _alarmId : Int = 0

    override fun connectDagger() {
        super.connectDagger()
        (activity?.application as BaseApplication).appComponent.planComponent().create()
            .historyComponent().create(_targetId).historyAlarmComponent().create(_alarmId).inject(this)
    }

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanHistoryAlarmBinding.inflate(inflater, container, false)
        binder.vm = alarmVM
        binder.lifecycleOwner = this
        binder.alarmId = _alarmId
        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUI()
        addEvent()
        onSubscribe()
    }

    private fun configureUI(){

    }

    private fun addEvent(){
        binder.repeatAlarmMondayTv.setOnClickListener { alarmVM.checkRepeatDay(DayOfWeek.MONDAY) }
        binder.repeatAlarmTuesdayTv.setOnClickListener { alarmVM.checkRepeatDay(DayOfWeek.TUESDAY) }
        binder.repeatAlarmWednesdayTv.setOnClickListener { alarmVM.checkRepeatDay(DayOfWeek.WEDNESDAY) }
        binder.repeatAlarmThursdayTv.setOnClickListener { alarmVM.checkRepeatDay(DayOfWeek.THURSDAY) }
        binder.repeatAlarmFridayTv.setOnClickListener { alarmVM.checkRepeatDay(DayOfWeek.FRIDAY) }
        binder.repeatAlarmSaturdayTv.setOnClickListener { alarmVM.checkRepeatDay(DayOfWeek.SATURDAY) }
        binder.repeatAlarmSundayTv.setOnClickListener { alarmVM.checkRepeatDay(DayOfWeek.SUNDAY) }
    }

    private fun onSubscribe(){
        alarmVM.closeEditor.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { close ->
                if(close) popCurrentController()
            }
        })
    }
}