package com.example.todoplusminus.ui.main.history.alarm.di

import com.example.todoplusminus.ui.main.history.alarm.PlanHistoryAlarmVM
import dagger.Module
import dagger.Provides

@Module
class PlanHistoryAlarmModule {

    @Provides
    fun provideAlarmVM() : PlanHistoryAlarmVM = PlanHistoryAlarmVM()
}