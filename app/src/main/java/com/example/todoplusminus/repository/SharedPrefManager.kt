package com.example.todoplusminus.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.todoplusminus.AppConfig

class SharedPrefManager(private val context: Context) {

    companion object {
        private const val PREFERENCE_NAME = "plan_preference"

        //setting attribute
        const val SUGGESTED_KEYWORD = "suggested_keyword"
        const val SWIPE_DIRECTION_TO_RIGHT = "swipe_direction_to_right"
        const val SHOW_CALENDAR = "show_calendar"
        const val TEXT_FONT = "text_font"
        const val ENABLE_ALARM = "enable_alarm"
    }

    private var mPreference: SharedPreferences? = null
    private var mEditor: SharedPreferences.Editor? = null

    private var sharedListener : SharedPreferences.OnSharedPreferenceChangeListener? = null

    init {
        mPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        mEditor = mPreference?.edit()

        populateAppConfig()
    }


    fun setSuggestedKeyword(wantShow: Boolean) {
        mEditor?.putBoolean(SUGGESTED_KEYWORD, wantShow)
        mEditor?.commit()

        setValueToAppConfig(SUGGESTED_KEYWORD, wantShow)
    }

    fun setShowCalendar(wantShow: Boolean) {
        mEditor?.putBoolean(SHOW_CALENDAR, wantShow)
        mEditor?.commit()

        setValueToAppConfig(SHOW_CALENDAR, wantShow)
    }

    fun setSwipeToRight(wantRight: Boolean) {
        mEditor?.putBoolean(SWIPE_DIRECTION_TO_RIGHT, wantRight)
        mEditor?.commit()

        setValueToAppConfig(SWIPE_DIRECTION_TO_RIGHT, wantRight)
    }

    fun setEnableAlarm(wantAlarm: Boolean) {
        mEditor?.putBoolean(ENABLE_ALARM, wantAlarm)
        mEditor?.commit()

        setValueToAppConfig(ENABLE_ALARM, wantAlarm)
    }

    fun getAllData() = mPreference?.all

    /**
     * sharedPreference에 저장된 내용을 AppConfig에 채운다.
     * */
    private fun populateAppConfig(){
        val dataMap = getAllData()

        dataMap?.keys?.forEach { key ->
            val value = dataMap[key]
            setValueToAppConfig(key, value as Boolean)
        }
    }

    /**
     * AppConfig에 데이터를 전달한다.
     * */
    private fun setValueToAppConfig(key : String, value : Boolean){
        when(key){
            SUGGESTED_KEYWORD -> AppConfig.showSuggestedKeyword = value
            SHOW_CALENDAR -> AppConfig.showCalendar = value
            SWIPE_DIRECTION_TO_RIGHT -> AppConfig.swipeDerectionToRight = value
            ENABLE_ALARM -> AppConfig.enableAlarm = value
        }
    }
}