package com.example.todoplusminus.ui.setting.di

import com.example.todoplusminus.ui.setting.SettingController
import com.example.todoplusminus.ui.setting.fontSetting.di.FontSettingComponent
import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [SettingModule::class, SettingVMModule::class, SettingSubComponent::class])
interface SettingComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create() : SettingComponent
    }

    fun fontSettingComponent() : FontSettingComponent.Factory

    fun inject(settingVC : SettingController)
}

@Module(subcomponents = [FontSettingComponent::class])
class SettingSubComponent()