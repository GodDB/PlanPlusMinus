package com.example.todoplusminus.repository

class SettingRepository(private val sharedPrefManager: SharedPrefManager) {

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