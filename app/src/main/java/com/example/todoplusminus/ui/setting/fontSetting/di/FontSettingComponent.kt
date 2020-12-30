package com.example.todoplusminus.ui.setting.fontSetting.di

import com.example.todoplusminus.ui.setting.fontSetting.FontSettingController
import dagger.Subcomponent


@Subcomponent(modules = [FontSettingModule::class])
interface FontSettingComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create() : FontSettingComponent
    }

    fun inject(fontSettingVC : FontSettingController)
}