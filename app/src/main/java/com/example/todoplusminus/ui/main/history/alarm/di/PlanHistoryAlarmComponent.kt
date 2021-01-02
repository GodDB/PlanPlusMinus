package com.example.todoplusminus.ui.main.history.alarm.di

import com.example.todoplusminus.ui.main.history.alarm.PlanHistoryAlarmController
import dagger.Subcomponent

@Subcomponent(modules = [PlanHistoryAlarmModule::class])
interface PlanHistoryAlarmComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create() : PlanHistoryAlarmComponent
    }

    fun inject(alarmVC : PlanHistoryAlarmController)
}