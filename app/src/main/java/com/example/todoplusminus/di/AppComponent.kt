package com.example.todoplusminus.di

import android.content.Context
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.local.ILocalDataSource
import com.example.todoplusminus.data.source.local.LocalDataSourceImpl
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import com.example.todoplusminus.data.source.local.db.PlannerDatabase
import com.example.todoplusminus.receivers.RebootReceiverComponent
import com.example.todoplusminus.ui.main.di.MainPlannerComponent
import com.example.todoplusminus.ui.setting.di.SettingComponent
import com.example.todoplusminus.ui.splash.di.SplashComponent
import com.example.todoplusminus.ui.tracker.di.TrackerComponent
import com.example.todoplusminus.util.AlarmManagerHelper
import dagger.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Singleton
@Component(modules = [SubcomponentModule::class, ViewModelBuilderModule::class, CommonModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun splashComponent(): SplashComponent.Factory
    fun planComponent(): MainPlannerComponent.Factory
    fun settingComponent(): SettingComponent.Factory
    fun trackerComponent(): TrackerComponent.Factory
    fun rebootReceiverComponent() : RebootReceiverComponent.Factory

}


@Module(subcomponents = [SplashComponent::class, MainPlannerComponent::class, SettingComponent::class, TrackerComponent::class, RebootReceiverComponent::class])
class SubcomponentModule

@Module
class CommonModule {
    @Singleton
    @Provides
    fun provideDB(context: Context): PlannerDatabase {
        return PlannerDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideSharedPrefManager(context: Context): SharedPrefManager {
        return SharedPrefManager(
            context
        )
    }

    @Singleton
    @Provides
    fun provideFontManager(context: Context): FontDownloadManager =
        FontDownloadManager(context)

    @Singleton
    @Provides
    fun provideLocalSource(db: PlannerDatabase): ILocalDataSource = LocalDataSourceImpl(db)

    @Provides
    fun provideAlarmHelper(context: Context): AlarmManagerHelper = AlarmManagerHelper(context)

    @Singleton
    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO
}