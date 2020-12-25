package com.example.todoplusminus.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.repository.SettingRepository
import java.lang.ClassCastException

class PlannerVMFactory(private val repository: PlannerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlannerViewModel(repository) as T
    }
}


class SettingVMFactory(private val repository: SettingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingVM(repository) as T
    }
}

