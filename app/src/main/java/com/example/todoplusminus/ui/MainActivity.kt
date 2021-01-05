package com.example.todoplusminus.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.ui.splash.SplashController
import com.example.todoplusminus.databinding.ActivityMainBinding
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.repository.SplashRepository
import com.example.todoplusminus.ui.splash.SplashVM
import com.example.todoplusminus.util.AlarmManagerHelper
import com.example.todoplusminus.util.AlarmReceiver
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class MainActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        router = Conductor.attachRouter(this, vb.main, savedInstanceState)

      /*  testAlarm(1, "갓1", 0)
        testAlarm(1, "갓2", 1200)
        testAlarm(1, "갓3", 2400)
        testAlarm(1, "갓4", 3600)
        testAlarm(1, "갓5", 4800)
        testAlarm(1, "갓6",6000)
        testAlarm(1, "갓7",7200)*/

        if (!router.hasRootController())
            router.setRoot(RouterTransaction.with(SplashController()))
    }

    override fun onBackPressed() {
        if (!router.handleBack())
            super.onBackPressed()
    }


    fun testAlarm(requestCode : Int, title : String, timeInterval : Int){

/*        alarmManager.cancel(pendingIntent)*/
       /* alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            60,
            pendingIntent
        )*/

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra(AlarmManagerHelper.TITLE_ID, title)

        val pendingIntent = PendingIntent.getBroadcast(
            this, requestCode, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        /*val triggerTime = SystemClock.elapsedRealtime() + timeInterval

        Log.d()*/

        val triggerTime = System.currentTimeMillis()

        val a = LocalDateTime.ofInstant(Instant.ofEpochMilli(triggerTime), ZoneId.systemDefault())
        Log.d("godgod", "${a}")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }

}