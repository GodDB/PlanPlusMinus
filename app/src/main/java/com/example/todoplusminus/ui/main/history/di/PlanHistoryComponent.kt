package com.example.todoplusminus.ui.main.history.di

import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.ui.main.history.PlanHistoryController
import com.example.todoplusminus.ui.main.history.PlanHistoryVM
import com.example.todoplusminus.ui.main.history.alarm.di.PlanHistoryAlarmComponent
import com.example.todoplusminus.ui.main.history.chart.di.PlanHistoryContentComponent
import com.example.todoplusminus.ui.main.history.chart.di.PlanHistoryContentModule
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent


@Subcomponent(modules = [PlanHistoryModule::class, PlanHistorySubComponentModule::class])
interface PlanHistoryComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance targetId : BaseID) : PlanHistoryComponent
    }

    fun historyContentComponent() : PlanHistoryContentComponent.Factory
    fun historyAlarmComponent() : PlanHistoryAlarmComponent.Factory

    fun inject(planHistoryVC : PlanHistoryController)
}

@Module(subcomponents = [PlanHistoryContentComponent::class, PlanHistoryAlarmComponent::class])
class PlanHistorySubComponentModule