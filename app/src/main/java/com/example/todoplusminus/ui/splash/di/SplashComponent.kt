package com.example.todoplusminus.ui.splash.di

import com.example.todoplusminus.data.repository.SplashRepository
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import com.example.todoplusminus.ui.splash.SplashController
import com.example.todoplusminus.ui.splash.SplashVM
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [SplashModule::class])
interface SplashComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create() : SplashComponent
    }

    fun inject(splashVC : SplashController)
}

@Module
class SplashModule{

    @Provides
    fun provideSplashVM(repo : SplashRepository) : SplashVM =
        SplashVM(repo)


    @Provides
    fun provideSplashRepo(prefManager: SharedPrefManager, fontDownloadManager: FontDownloadManager) : SplashRepository {
        return SplashRepository(
            prefManager,
            fontDownloadManager
        )
    }
}