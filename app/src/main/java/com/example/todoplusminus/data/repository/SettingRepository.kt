package com.example.todoplusminus.data.repository

import android.graphics.Typeface
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.data.source.local.ILocalDataSource
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import com.example.todoplusminus.util.PMCoroutineSpecification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
    private val fontManager: FontDownloadManager,
    private val localDataSource : ILocalDataSource,
    private val dispatcher: CoroutineDispatcher
) : ISettingRepository {

    override suspend fun getFont(fontName: String) : Typeface? {
        val font = fontManager.downloadFont(fontName)
        registerFont(font, fontName)

        return font
    }

    override fun setSuggestedKeyword(wantShow: Boolean) {
        sharedPrefManager.setSuggestedKeyword(wantShow)
    }

    override fun setShowCalendar(wantShow: Boolean) {
        sharedPrefManager.setShowCalendar(wantShow)
    }

    override fun setSwipeToRight(wantRight: Boolean) {
        sharedPrefManager.setSwipeToRight(wantRight)
    }

    override fun setEnableAlarm(wantAlarm: Boolean) {
        sharedPrefManager.setEnableAlarm(wantAlarm)
    }

    override fun setPlanSize(planSize : Int){
        sharedPrefManager.setPlanSize(planSize)
    }

    override suspend fun onDeleteAllData(){
        withContext(dispatcher){
            localDataSource.deleteAllData()
        }
    }

    private fun registerFont(font : Typeface?, fontName : String){
        if(font != null) sharedPrefManager.setFontName(fontName)
        AppConfig.font = font
    }

}