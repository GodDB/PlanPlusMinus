package com.example.todoplusminus.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoplusminus.data.repository.PlannerRepository
import com.example.todoplusminus.data.repository.SettingRepository
import com.example.todoplusminus.ui.main.PlannerViewModel
import com.example.todoplusminus.ui.setting.SettingVM

/*class PlannerVMFactory(private val repository: PlannerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlannerViewModel(repository) as T
    }
}*/


class SettingVMFactory(private val repository: SettingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingVM(repository) as T
    }
}

