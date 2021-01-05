package com.example.todoplusminus.base

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.todoplusminus.di.AppComponent
import com.example.todoplusminus.di.DaggerAppComponent


class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //다크모드 비활성
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    val appComponent : AppComponent by lazy {
        initializeComponent()
    }

    private fun initializeComponent() : AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }


}