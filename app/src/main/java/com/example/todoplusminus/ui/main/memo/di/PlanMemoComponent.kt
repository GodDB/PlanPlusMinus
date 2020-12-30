package com.example.todoplusminus.ui.main.memo.di

import com.example.todoplusminus.ui.main.memo.PlanMemoController
import dagger.BindsInstance
import dagger.Subcomponent
import java.time.LocalDate

@Subcomponent(modules = [PlanMemoModule::class])
interface PlanMemoComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance targetDate : LocalDate) : PlanMemoComponent
    }

    fun inject(planMemoVC : PlanMemoController)
}