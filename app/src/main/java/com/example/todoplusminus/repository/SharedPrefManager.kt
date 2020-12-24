package com.example.todoplusminus.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.todoplusminus.AppConfig

class SharedPrefManager(private val context: Context) {

    companion object {
        private const val PREFERENCE_NAME = "plan_preference"

        //setting attribute
        const val SUGGESTED_KEYWORD = "suggested_keyword"
        const val SWIPE_DIRECTION_TO_RIGHT = "swipe_direction_to_right"
        const val SHOW_CALENDAR = "show_calendar"
        const val TEXT_FONT_NAME = "text_font"
        const val ENABLE_ALARM = "enable_alarm"
    }

    private var mPreference: SharedPreferences? = null
    private var mEditor: SharedPreferences.Editor? = null


    init {
        mPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        mEditor = mPreference?.edit()
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

    fun setFontName(fontName : String){
        mEditor?.putString(TEXT_FONT_NAME, fontName)
        mEditor?.commit()

        setValueToAppConfig(TEXT_FONT_NAME, fontName)
    }

    fun getAllData() = mPreference?.all

    /**
     * sharedPreference에 저장된 내용을 AppConfig에 채운다.
     * */
    fun populateAppConfig(){
        val dataMap = getAllData()

        dataMap?.keys?.forEach { key ->
            when(dataMap[key]){
                is String -> setValueToAppConfig(key, dataMap[key] as String)
                is Boolean -> setValueToAppConfig(key, dataMap[key] as Boolean)
            }
        }
        Log.d("godgod", "populate")
    }

    /**
     * AppConfig에 데이터를 전달한다.
     * */
    private fun setValueToAppConfig(key : String, value : Boolean){
        when(key){
            SUGGESTED_KEYWORD -> AppConfig.showSuggestedKeyword = value
            SHOW_CALENDAR -> AppConfig.showCalendar = value
            SWIPE_DIRECTION_TO_RIGHT -> AppConfig.swipeDirectionToRight = value
            ENABLE_ALARM -> AppConfig.enableAlarm = value
        }
    }

    private fun setValueToAppConfig(key : String, value : String){
        when(key){
            TEXT_FONT_NAME -> AppConfig.fontName = value
        }
    }
}