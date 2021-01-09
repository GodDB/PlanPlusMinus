package com.example.todoplusminus.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.example.todoplusminus.R
import com.example.todoplusminus.ui.splash.SplashController
import com.example.todoplusminus.databinding.ActivityMainBinding
import com.example.todoplusminus.util.AlarmManagerHelper
import com.example.todoplusminus.receivers.AlarmReceiver
import com.example.todoplusminus.ui.common.CommonDialogController
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class MainActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        router = Conductor.attachRouter(this, vb.main, savedInstanceState)

        if (!router.hasRootController())
            router.setRoot(RouterTransaction.with(SplashController()))
    }

    override fun onBackPressed() {
        //router가 handleback을 처리할 수 없을 때 (즉, 백스택이 비었을 떼)
        if (!router.handleBack())
            super.onBackPressed()

    }
}