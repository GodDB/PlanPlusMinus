package com.example.todoplusminus.base

import android.app.Application
import com.example.todoplusminus.di.AppComponent
import com.example.todoplusminus.di.DaggerAppComponent


class BaseApplication : Application() {

    val appComponent : AppComponent by lazy {
        initializeComponent()
    }

    private fun initializeComponent() : AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }


}