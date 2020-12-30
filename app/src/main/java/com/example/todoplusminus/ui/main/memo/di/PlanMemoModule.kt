package com.example.todoplusminus.ui.main.memo.di

import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.ui.main.memo.PlanMemoVM
import dagger.Module
import dagger.Provides
import java.time.LocalDate

@Module
class PlanMemoModule {

/*    @Provides
    fun provideTargetData(date : LocalDate) : LocalDate = date
    */
    @Provides
    fun providePlanMemoVM(repo : IPlannerRepository, targetDate : LocalDate) : PlanMemoVM {
        return PlanMemoVM(repo, targetDate)
    }
}