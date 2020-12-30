package com.example.todoplusminus.ui.setting.di

import androidx.lifecycle.ViewModel
import com.example.todoplusminus.data.repository.SettingRepository
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.local.ILocalDataSource
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import com.example.todoplusminus.di.AACViewModelKey
import com.example.todoplusminus.ui.setting.SettingVM
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.CoroutineDispatcher

@Module
class SettingModule {

    @Provides
    fun provideSettingRepo(sharedPrefManager: SharedPrefManager,
                           fontManager: FontDownloadManager,
                           localDataSource : ILocalDataSource,
                           dispatcher: CoroutineDispatcher
    ) : SettingRepository = SettingRepository(sharedPrefManager, fontManager, localDataSource, dispatcher)
}

@Module
abstract class SettingVMModule{
    @Binds
    @IntoMap
    @AACViewModelKey(SettingVM::class)
    abstract fun provideViewModel(viewModel: SettingVM) : ViewModel
}

