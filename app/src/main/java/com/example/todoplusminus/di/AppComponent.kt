package com.example.todoplusminus.di

import android.content.Context
import com.example.todoplusminus.data.repository.SplashRepository
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import com.example.todoplusminus.ui.splash.SplashVM
import dagger.*


@Component(modules = [SubcomponentModule::class, RepositoryModule::class])
interface AppComponent{

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance applicationContext : Context) : AppComponent
    }

    fun mainComponent() : MainComponent.Factory

}


@Module(subcomponents = [MainComponent::class])
class SubcomponentModule{}

/*@Module
class ApplicationModule{
    @Provides
    fun provideContext(application: Application) : Context = application
}*/

@Module
class RepositoryModule{
/*
    @Provides
    fun provideDB(application: Application) : PlannerDatabase{
        return PlannerDatabase.getInstance(application.applicationContext)
    }*/

    @Provides
    fun provideSplashVM(repo : SplashRepository) : SplashVM =
        SplashVM(repo)

    @Provides
    fun provideSharedPrefManager(context: Context) : SharedPrefManager {
        return SharedPrefManager(
            context
        )
    }

    @Provides
    fun provideSplashRepo(prefManager: SharedPrefManager, fontDownloadManager: FontDownloadManager) : SplashRepository {
        return SplashRepository(
            prefManager,
            fontDownloadManager
        )
    }

    @Provides
    fun provideFontManager(context : Context) : FontDownloadManager =
        FontDownloadManager(context)

/*    @Provides
    fun providePlannerLocalSource(db : PlannerDatabase) : ILocalDataSource{
        return LocalDataSourceImpl(db)
    }

    @Provides
    fun providePlannerRepo(manager : SharedPrefManager, dataSource : ILocalDataSource) : IPlannerRepository{
        return PlannerRepository(sharedPrefManager = manager, localSource = dataSource)
    }*/
}