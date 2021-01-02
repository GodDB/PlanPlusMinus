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

class PlanHistoryAlarmController : DBControllerBase {

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(targetId: BaseID) {
        this._targetId = targetId
    }

    @Inject
    lateinit var alarmVM: PlanHistoryAlarmVM
    private lateinit var binder: ControllerPlanHistoryAlarmBinding
    private lateinit var _targetId: BaseID

    override fun connectDagger() {
        super.connectDagger()
        (activity?.application as BaseApplication).appComponent.planComponent().create()
            .historyComponent().create(_targetId).historyAlarmComponent().create().inject(this)
    }

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanHistoryAlarmBinding.inflate(inflater, container, false)
        binder.vm = alarmVM
        binder.lifecycleOwner = this
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

        alarmVM.repeatAlarmToMonday.observe(this, Observer { value ->
            if(value) binder.repeatAlarmMondayCheckView.visibility = View.VISIBLE
            else binder.repeatAlarmMondayCheckView.visibility = View.GONE
        })

        alarmVM.repeatAlarmToTuesday.observe(this, Observer { value ->
            if(value) binder.repeatAlarmTuesdayCheckView.visibility = View.VISIBLE
            else binder.repeatAlarmTuesdayCheckView.visibility = View.GONE
        })

        alarmVM.repeatAlarmToWednesday.observe(this, Observer { value ->
            if(value) binder.repeatAlarmWednesdayCheckView.visibility = View.VISIBLE
            else binder.repeatAlarmWednesdayCheckView.visibility = View.GONE
        })

        alarmVM.repeatAlarmToThursday.observe(this, Observer { value ->
            if(value) binder.repeatAlarmThursdayCheckView.visibility = View.VISIBLE
            else binder.repeatAlarmThursdayCheckView.visibility = View.GONE
        })

        alarmVM.repeatAlarmToFriday.observe(this, Observer { value ->
            if(value) binder.repeatAlarmFridayCheckView.visibility = View.VISIBLE
            else binder.repeatAlarmFridayCheckView.visibility = View.GONE
        })

        alarmVM.repeatAlarmToSaturday.observe(this, Observer { value ->
            if(value) binder.repeatAlarmSaturdayCheckView.visibility = View.VISIBLE
            else binder.repeatAlarmSaturdayCheckView.visibility = View.GONE
        })

        alarmVM.repeatAlarmToSunday.observe(this, Observer { value ->
            if(value) binder.repeatAlarmSundayCheckView.visibility = View.VISIBLE
            else binder.repeatAlarmSundayCheckView.visibility = View.GONE
        })
    }
}