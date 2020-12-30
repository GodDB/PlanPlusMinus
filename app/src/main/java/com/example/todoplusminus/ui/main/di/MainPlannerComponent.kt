package com.example.todoplusminus.ui.main.di


import com.example.todoplusminus.ui.main.PlannerController
import com.example.todoplusminus.ui.main.edit.di.PlanEditComponent
import com.example.todoplusminus.ui.main.history.di.PlanHistoryComponent
import com.example.todoplusminus.ui.main.memo.di.PlanMemoComponent
import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [MainPlannerSubComponent::class, MainPlannerModule::class, MainPlannerVMModule::class])
interface MainPlannerComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainPlannerComponent
    }

    fun memoComponent() : PlanMemoComponent.Factory
    fun editComponent() : PlanEditComponent.Factory
    fun historyComponent() : PlanHistoryComponent.Factory

    fun inject(mainPlanVC: PlannerController)
}

@Module(subcomponents = [PlanMemoComponent::class, PlanEditComponent::class, PlanHistoryComponent::class])
class MainPlannerSubComponent
