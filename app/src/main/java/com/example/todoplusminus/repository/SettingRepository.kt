package com.example.todoplusminus.repository

import android.graphics.Typeface

class SettingRepository(private val sharedPrefManager: SharedPrefManager, private val fontManager : FontDownloadManager) {

    fun getFont(fontName : String, onComplete: (Typeface) -> Unit, onError : () -> Unit ){
        val onComplete2 : (Typeface) -> Unit = { typeface ->
            sharedPrefManager.setFontName(fontName)
            onComplete(typeface)
        }

        fontManager.downloadFont(fontName, onComplete2, onError)
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

}