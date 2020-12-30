package com.example.todoplusminus.ui.tracker.di

import com.example.todoplusminus.ui.tracker.TrackerController
import dagger.Subcomponent


@Subcomponent(modules = [TrackerModule::class])
interface TrackerComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create() : TrackerComponent
    }

    fun inject(trackerVC : TrackerController)
}