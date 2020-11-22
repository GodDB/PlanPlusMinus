package com.example.todoplusminus.util

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

object VibrateHelper {

    private var mVibrator : Vibrator? = null

    fun setUp(context : Context){
        mVibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun start(){
        //deprecated 됬지만 대체안이 sdk26 이라 그냥 사용할 수 없어, deprecated된거 그대로 사용한다.
       mVibrator?.vibrate(2)
    }
}