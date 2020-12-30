package com.example.todoplusminus.ui.tracker.di

import com.example.todoplusminus.data.repository.TrackerRepository
import com.example.todoplusminus.data.source.local.ILocalDataSource
import com.example.todoplusminus.data.source.local.LocalDataSourceImpl
import com.example.todoplusminus.ui.tracker.TrackerVM
import dagger.Module
import dagger.Provides


@Module
class TrackerModule {

    @Provides
    fun provideTrackerRepo(localSource : ILocalDataSource) : TrackerRepository{
        return TrackerRepository(localSource)
    }

    @Provides
    fun provideTrackerVM(repo : TrackerRepository) : TrackerVM{
        return TrackerVM((repo))
    }
}