package com.example.todoplusminus.ui.main.edit.di

import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.data.repository.PlannerRepository
import com.example.todoplusminus.ui.main.edit.PlanEditVM
import dagger.Module
import dagger.Provides


@Module
class PlanEditModule {

    @Provides
    fun providePlanEditVM(repository: IPlannerRepository, targetId: String): PlanEditVM =
        PlanEditVM(repository).apply { setId(targetId) }
}