package com.example.todoplusminus.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.todoplusminus.base.BaseApplication
import com.example.todoplusminus.data.repository.ISettingRepository
import com.example.todoplusminus.data.repository.SettingRepository
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.local.ILocalDataSource
import com.example.todoplusminus.data.source.local.db.PlannerDatabase
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import com.example.todoplusminus.util.AlarmManagerHelper
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//디바이스 재 부팅을 감지하는 broadcast receiver
class RebootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repo: ISettingRepository

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("godgod", "reboot receive!")
        connectDagger(context)

        //사용자가 등록한 모든 알람을 재 등록한다.
        CoroutineScope(Dispatchers.Main).launch {
            repo.reloadAllAlarm()
        }
    }


    private fun connectDagger(context: Context) {
        (context.applicationContext as BaseApplication)
            .appComponent
            .rebootReceiverComponent()
            .create()
            .inject(this)
    }
}


/** di */

@Subcomponent(modules = [RebootReceiverModule::class])
interface RebootReceiverComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RebootReceiverComponent
    }

    fun inject(rebootReceiver: RebootReceiver)
}

@Module
class RebootReceiverModule {

    @Provides
    fun provideSettingRepo(
        sharedPrefManager: SharedPrefManager,
        fontManager: FontDownloadManager,
        localDataSource: ILocalDataSource,
        alarmHelepr: AlarmManagerHelper,
        dispatcher: CoroutineDispatcher
    ): ISettingRepository =
        SettingRepository(sharedPrefManager, fontManager, localDataSource, alarmHelepr, dispatcher)
}
