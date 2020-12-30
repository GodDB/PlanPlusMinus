package com.example.todoplusminus.ui.main.edit.di

import com.example.todoplusminus.ui.main.edit.PlanEditController
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [PlanEditModule::class])
interface PlanEditComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance targetId : String) : PlanEditComponent
    }

    fun inject(planEditVC : PlanEditController)
}