package com.example.todoplusminus.ui.main.history.di

import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.ui.main.history.PlanHistoryVM
import com.example.todoplusminus.ui.main.history.chart.PlanHistoryChartAdapter
import dagger.Module
import dagger.Provides

@Module
class PlanHistoryModule {

    @Provides
    fun provideHistoryVM(targetId : BaseID, repo : IPlannerRepository) : PlanHistoryVM{
        return PlanHistoryVM(targetId, repo)
    }
}