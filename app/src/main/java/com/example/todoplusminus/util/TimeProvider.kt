package com.example.todoplusminus.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object TimeProvider {

    fun getCurAllDate() : String{
        val curMills = System.currentTimeMillis()
        val date = Date(curMills)
        val result = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        return result.format(date)
    }

    fun getCurDate() : String{
        val curMills = System.currentTimeMillis()
        val date = Date(curMills)
        val result = SimpleDateFormat("yyyy/MM/dd")
        return result.format(date)
    }

    fun getCurDateTime() : String{
        val curMills = System.currentTimeMillis()
        val date = Date(curMills)
        val result = SimpleDateFormat("MM/dd\n a HH:mm")
        return result.format(date)
    }

    fun getCurTime() : String{
        val curMills = System.currentTimeMillis()
        val date = Date(curMills)
        val result = SimpleDateFormat("HH:mm:ss")
        return result.format(date)
    }
}