package com.example.todoplusminus.data.repository

import android.graphics.Typeface
import kotlinx.coroutines.withContext

interface ISettingRepository {

    suspend fun getFont(fontName: String) : Typeface?

    fun setSuggestedKeyword(wantShow: Boolean)

    fun setShowCalendar(wantShow: Boolean)

    fun setSwipeToRight(wantRight: Boolean)

    fun setEnableAlarm(wantAlarm: Boolean)

    fun setPlanSize(planSize : Int)

    suspend fun onDeleteAllData()

}