package com.example.todoplusminus.repository

import android.graphics.Typeface
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.PMCoroutineSpecification
import kotlinx.coroutines.CoroutineDispatcher

class SettingRepository(
    private val sharedPrefManager: SharedPrefManager,
    private val fontManager: FontDownloadManager,
    private val dispatcher: CoroutineDispatcher = PMCoroutineSpecification.MAIN_DISPATCHER
) {

    suspend fun getFont(fontName: String) : Typeface? {
        val font = fontManager.downloadFont(fontName)
        registerFont(font, fontName)

        return font
    }

    fun setSuggestedKeyword(wantShow: Boolean) {
        sharedPrefManager.setSuggestedKeyword(wantShow)
    }

    fun setShowCalendar(wantShow: Boolean) {
        sharedPrefManager.setShowCalendar(wantShow)
    }

    fun setSwipeToRight(wantRight: Boolean) {
        sharedPrefManager.setSwipeToRight(wantRight)
    }

    fun setEnableAlarm(wantAlarm: Boolean) {
        sharedPrefManager.setEnableAlarm(wantAlarm)
    }

    private fun registerFont(font : Typeface?, fontName : String){
        if(font != null) sharedPrefManager.setFontName(fontName)
        AppConfig.font = font
    }

}