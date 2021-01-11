package com.example.todoplusminus.data.source.file

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.util.BooleanPreference
import com.example.todoplusminus.util.asFlow
import kotlinx.coroutines.flow.Flow

class SharedPrefManager(private val context: Context) {

    companion object {
        private const val PREFERENCE_NAME = "plan_preference"

        //tooltip을 보여줄 것인지 여부(앱 첫 실행시에만 보여진다)
        const val FIRST_TIME_RUNNING_TOOLTIP1 = "first_time_running_tooltip1"
        const val FIRST_TIME_RUNNING_TOOLTIP2 = "first_time_running_tooltip2"
        const val FIRST_TIME_RUNNING_TOOLTIP3 = "first_time_running_tooltip3"
        const val FIRST_TIME_RUNNING_TOOLTIP4 = "first_time_running_tooltip4"

        //setting attribute
        const val SUGGESTED_KEYWORD = "suggested_keyword"
        const val SWIPE_DIRECTION_TO_RIGHT = "swipe_direction_to_right"
        const val SHOW_CALENDAR = "show_calendar"
        const val TEXT_FONT_NAME = "text_font"
        const val ENABLE_ALARM = "enable_alarm"
        const val PLAN_SIZE = "plan_size"
    }

    private var mPreference: SharedPreferences
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

    fun setPlanSize(planSize : Int){
        mEditor?.putInt(PLAN_SIZE, planSize)
        mEditor?.commit()

        setValueToAppConfig(PLAN_SIZE, planSize)
    }

    fun getAllData() = mPreference?.all


    //tooltip을 보여줄것인지? (앱 첫 실행시 )
    fun checkShowTooltip1() : Flow<Boolean> {
        val preference = BooleanPreference(mPreference, FIRST_TIME_RUNNING_TOOLTIP1, true)
        return preference.asFlow()
    }
    fun checkShowTooltip2() : Flow<Boolean>{
        val preference = BooleanPreference(mPreference, FIRST_TIME_RUNNING_TOOLTIP2, true)
        return preference.asFlow()
    }
    fun checkShowTooltip3() : Flow<Boolean>{
        val preference = BooleanPreference(mPreference, FIRST_TIME_RUNNING_TOOLTIP3, true)
        return preference.asFlow()
    }
    fun checkShowTooltip4() : Flow<Boolean>{
        val preference = BooleanPreference(mPreference, FIRST_TIME_RUNNING_TOOLTIP4, true)
        return preference.asFlow()
    }

    //tooltip을 보여주지 않아야 한다.
    fun enabledTooltip1(){
        mEditor?.putBoolean(FIRST_TIME_RUNNING_TOOLTIP1, false)?.apply()
    }
    fun enabledTooltip2(){
        mEditor?.putBoolean(FIRST_TIME_RUNNING_TOOLTIP2, false)?.apply()
    }
    fun enabledTooltip3(){
        mEditor?.putBoolean(FIRST_TIME_RUNNING_TOOLTIP3, false)?.apply()
    }
    fun enabledTooltip4(){
        mEditor?.putBoolean(FIRST_TIME_RUNNING_TOOLTIP4, false)?.apply()
    }



    /**
     * sharedPreference에 저장된 내용을 AppConfig에 채운다.
     * */
    fun populateAppConfig(){
        val dataMap = getAllData()

        dataMap?.keys?.forEach { key ->
            when(dataMap[key]){
                is String -> setValueToAppConfig(key, dataMap[key] as String)
                is Boolean -> setValueToAppConfig(key, dataMap[key] as Boolean)
                is Int -> setValueToAppConfig(key, dataMap[key] as Int)
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

    private fun setValueToAppConfig(key : String, value : Int){
        when(key){
            PLAN_SIZE -> AppConfig.planSize = value
        }
    }
}