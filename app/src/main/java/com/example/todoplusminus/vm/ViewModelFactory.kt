package com.example.todoplusminus.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoplusminus.repository.PlannerRepository

class ViewModelFactory(private val repository: PlannerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlannerViewModel(repository) as T
    }
}