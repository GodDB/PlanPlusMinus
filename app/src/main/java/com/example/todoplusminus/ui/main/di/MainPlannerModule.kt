package com.example.todoplusminus.ui.main.di

import androidx.lifecycle.ViewModel
import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.data.repository.PlannerRepository
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.local.ILocalDataSource
import com.example.todoplusminus.di.AACViewModelKey
import com.example.todoplusminus.ui.main.PlannerViewModel
import com.example.todoplusminus.util.AlarmManagerHelper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class MainPlannerModule() {

    @Provides
    fun providePlanRepo(
        localSource: ILocalDataSource,
        sharedPrefManager: SharedPrefManager,
        alarmHelper : AlarmManagerHelper,
        dispatcher: CoroutineDispatcher
    ) : IPlannerRepository {
        return PlannerRepository(localSource, sharedPrefManager, alarmHelper, dispatcher)
    }
}


@Module
abstract class MainPlannerVMModule{

    @Binds
    @IntoMap
    @AACViewModelKey(PlannerViewModel::class)
    abstract fun provideViewModel(viewModel: PlannerViewModel) : ViewModel
}
