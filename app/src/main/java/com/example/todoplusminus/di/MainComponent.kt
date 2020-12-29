package com.example.todoplusminus.di

import com.example.todoplusminus.ui.main.edit.PlanEditController
import com.example.todoplusminus.ui.splash.SplashController
import dagger.Subcomponent


@Subcomponent
interface MainComponent {

    @Subcomponent.Builder
    interface Factory{
        fun create() : MainComponent
    }

    fun inject(planEditor : PlanEditController)
    fun inject(splashEditor : SplashController)
}