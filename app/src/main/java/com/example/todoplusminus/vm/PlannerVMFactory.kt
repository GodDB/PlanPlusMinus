package com.example.todoplusminus.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoplusminus.repository.PlannerRepository
import java.lang.ClassCastException

class PlannerVMFactory(private val repository: PlannerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass){
            PlannerViewModel::class.java -> PlannerViewModel(repository) as T
            PlanEditVM::class.java -> PlanEditVM(repository) as T
            else -> throw ClassCastException()
        }
    }
}

