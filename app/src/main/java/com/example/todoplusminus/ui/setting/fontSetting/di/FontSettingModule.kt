package com.example.todoplusminus.ui.setting.fontSetting.di

import com.example.todoplusminus.data.repository.ISettingRepository
import com.example.todoplusminus.ui.setting.fontSetting.FontSettingVM
import dagger.Module
import dagger.Provides


@Module
class FontSettingModule {

    @Provides
    fun provideFontSettingVM(settingRepo : ISettingRepository) : FontSettingVM{
        return FontSettingVM(settingRepo)
    }
}