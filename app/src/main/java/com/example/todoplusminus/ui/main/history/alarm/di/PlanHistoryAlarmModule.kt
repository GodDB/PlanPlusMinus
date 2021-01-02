package com.example.todoplusminus.ui.main.history.alarm.di

import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.ui.main.history.alarm.PlanHistoryAlarmVM
import dagger.Module
import dagger.Provides

@Module
class PlanHistoryAlarmModule {

    @Provides
    fun provideAlarmVM(planId : BaseID, alarmId : Int, repo : IPlannerRepository) : PlanHistoryAlarmVM =
        PlanHistoryAlarmVM(planId, alarmId, repo)
}