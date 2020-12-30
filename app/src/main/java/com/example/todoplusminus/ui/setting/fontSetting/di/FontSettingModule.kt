package com.example.todoplusminus.ui.setting.fontSetting.di

import com.example.todoplusminus.data.repository.SettingRepository
import com.example.todoplusminus.ui.setting.fontSetting.FontSettingVM
import dagger.Module
import dagger.Provides


@Module
class FontSettingModule {

    @Provides
    fun provideFontSettingVM(settingRepo : SettingRepository) : FontSettingVM{
        return FontSettingVM(settingRepo)
    }
}