package com.example.todoplusminus.util

import android.content.Context
import android.util.DisplayMetrics

object DeviceManager {
    private var mDisplay : DisplayMetrics? = null

    fun setUp(context : Context){
        mDisplay = context.resources.displayMetrics
    }

    fun getDeviceWidth() : Float {
        if(mDisplay == null) return 0f

        return mDisplay!!.widthPixels.toFloat()
    }
}