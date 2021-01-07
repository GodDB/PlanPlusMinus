package com.example.todoplusminus.base

import android.app.Application
import android.content.ComponentName
import android.content.pm.PackageManager.*
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.todoplusminus.di.AppComponent
import com.example.todoplusminus.di.DaggerAppComponent
import com.example.todoplusminus.receivers.RebootReceiver


class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("godgod", "baseApplication onCreate")
        //다크모드 비활성
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //재부팅시 rebootAlarmReceiver를 실행시키게 한다.
        registerRebootReceiverToSystem()
    }

    val appComponent : AppComponent by lazy {
        initializeComponent()
    }

    private fun initializeComponent() : AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }


    private fun registerRebootReceiverToSystem(){
        val receiver = ComponentName(applicationContext, RebootReceiver::class.java)

        applicationContext.packageManager.setComponentEnabledSetting(
            receiver,
            COMPONENT_ENABLED_STATE_ENABLED,
            DONT_KILL_APP
        )
    }
}