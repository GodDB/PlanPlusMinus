package com.example.todoplusminus.ui.main.history.chart.di

import com.example.todoplusminus.ui.main.history.chart.PlanHistoryContentVM
import com.example.todoplusminus.ui.main.history.chart.PlanHistoryContentsController
import dagger.BindsInstance
import dagger.Subcomponent


@Subcomponent(modules = [PlanHistoryContentModule::class])
interface PlanHistoryContentComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance mode : PlanHistoryContentVM.Mode) : PlanHistoryContentComponent
    }

    fun inject(PlanHistoryContentVC : PlanHistoryContentsController)
}