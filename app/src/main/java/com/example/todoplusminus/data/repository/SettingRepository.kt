package com.example.todoplusminus.data.repository

import android.graphics.Typeface
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.data.source.local.ILocalDataSource
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import com.example.todoplusminus.util.PMCoroutineSpecification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SettingRepository(
    private val sharedPrefManager: SharedPrefManager,
    private val fontManager: FontDownloadManager,
    private val localDataSource : ILocalDataSource,
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

    fun setPlanSize(planSize : Int){
        sharedPrefManager.setPlanSize(planSize)
    }

    suspend fun onDeleteAllData(){
        withContext(dispatcher){
            localDataSource.deleteAllData()
        }
    }

    private fun registerFont(font : Typeface?, fontName : String){
        if(font != null) sharedPrefManager.setFontName(fontName)
        AppConfig.font = font
    }

}