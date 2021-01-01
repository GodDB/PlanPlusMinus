package com.example.todoplusminus.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.ui.splash.SplashController
import com.example.todoplusminus.databinding.ActivityMainBinding
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.repository.SplashRepository
import com.example.todoplusminus.ui.splash.SplashVM
import com.example.todoplusminus.util.AlarmReceiver

class MainActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        router = Conductor.attachRouter(this, vb.main, savedInstanceState)

       /* testAlarm(1, "갓1", 0)
        testAlarm(2, "갓2", 12000)
        testAlarm(3, "갓3", 24000)
        testAlarm(4, "갓4", 36000)
        testAlarm(5, "갓5", 48000)
        testAlarm(6, "갓6",60000)
        testAlarm(7, "갓7",72000)*/

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
        intent.putExtra("test", title)

        val pendingIntent = PendingIntent.getBroadcast(
            this, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val triggerTime = SystemClock.elapsedRealtime() + timeInterval

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }

}