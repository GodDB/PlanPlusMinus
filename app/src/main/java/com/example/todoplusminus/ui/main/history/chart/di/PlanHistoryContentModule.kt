package com.example.todoplusminus.ui.main.history.chart.di

import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.data.repository.PlannerRepository
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.local.ILocalDataSource
import com.example.todoplusminus.ui.main.history.chart.PlanHistoryContentVM
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
class PlanHistoryContentModule {

    @Provides
    fun providePlanHistoryContentVM(mode : PlanHistoryContentVM.Mode, targetId : BaseID, repo : IPlannerRepository) : PlanHistoryContentVM{
        return PlanHistoryContentVM(mode, targetId, repo)
    }
}