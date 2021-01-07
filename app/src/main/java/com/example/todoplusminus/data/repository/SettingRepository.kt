package com.example.todoplusminus.data.repository

import android.graphics.Typeface
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.data.entities.PlanAlarmData
import com.example.todoplusminus.data.source.local.ILocalDataSource
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import com.example.todoplusminus.data.source.local.db.PlannerAlarmEntity
import com.example.todoplusminus.data.source.local.db.PlannerItemAlarm
import com.example.todoplusminus.util.AlarmManagerHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
    private val fontManager: FontDownloadManager,
    private val localDataSource: ILocalDataSource,
    private val alarmHelper: AlarmManagerHelper,
    private val dispatcher: CoroutineDispatcher
) : ISettingRepository {

    override suspend fun getFont(fontName: String): Typeface? {
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

    override fun setEnableDefaultAlarm(wantAlarm: Boolean) {
        sharedPrefManager.setEnableAlarm(wantAlarm)

        registerOrUnregisterDefaultAlarm(wantAlarm)
    }

    override fun setPlanSize(planSize: Int) {
        sharedPrefManager.setPlanSize(planSize)
    }

    override suspend fun onDeleteAllData() {
        withContext(dispatcher) {
            //알람 제거
            localDataSource.getAllAlarmData()
                .take(1)
                .map { convertAlarmItemListToAlarmDataList(it) }
                .collect {
                    unregisterAlarmList(it)
                }

            //planData & memo 제거
            localDataSource.deleteAllData()

        }
    }

    override suspend fun reloadAllAlarm() {
        withContext(dispatcher){
            localDataSource.getAllAlarmData()
                .take(1)
                .map { convertAlarmItemListToAlarmDataList(it) }
                .collect {
                    registerRepeatAlarmList(it)
                }
        }
    }

    private fun registerOrUnregisterDefaultAlarm(wantAlarm: Boolean){
        if(wantAlarm) return alarmHelper.registerDefaultAlarm()

        alarmHelper.unregisterDefaultAlarm()
    }

    private fun registerFont(font: Typeface?, fontName: String) {
        if (font != null) sharedPrefManager.setFontName(fontName)
        AppConfig.font = font
    }

    private fun unregisterAlarmList(alarmDataList: List<PlanAlarmData>) {
        alarmDataList.forEach {
            unregisterAlarm(it)
        }
    }

    private fun registerRepeatAlarmList(alarmDataList : List<PlanAlarmData>) {
        alarmDataList.forEach {
            registerRepeatAlarm(it)
        }
    }

    private fun registerRepeatAlarm(alarmData: PlanAlarmData) {
        alarmHelper.registerAlarm(alarmData, AlarmManagerHelper.TYPE_REPEAT)
    }

    private fun unregisterAlarm(alarmData: PlanAlarmData) {
        alarmHelper.unregisterAlarm(alarmData)
    }


    companion object {
        fun convertAlarmItemListToAlarmDataList(alarmItemList: List<PlannerItemAlarm>): List<PlanAlarmData> {
            return alarmItemList.map {
                convertAlarmItemToAlarmData(it)
            }
        }

        fun convertAlarmItemToAlarmData(alarmItem: PlannerItemAlarm): PlanAlarmData {
            return PlanAlarmData(
                planId = alarmItem.planItem.id,
                planTitle = alarmItem.planItem.title,
                alarmId = alarmItem.planAlarm.alarmId,
                alarmTime = alarmItem.planAlarm.alarmTime,
                alarmRepeatMonday = alarmItem.planAlarm.alarmMonday,
                alarmRepeatTuesday = alarmItem.planAlarm.alarmTuesday,
                alarmRepeatWednesday = alarmItem.planAlarm.alarmWednesday,
                alarmRepeatThursday = alarmItem.planAlarm.alarmThursday,
                alarmRepeatFriday = alarmItem.planAlarm.alarmFriday,
                alarmRepeatSaturday = alarmItem.planAlarm.alarmSaturday,
                alarmRepeatSunday = alarmItem.planAlarm.alarmSunday
            )
        }

        fun convertAlarmDataToAlarmEntity(alarmData: PlanAlarmData): PlannerAlarmEntity {
            return PlannerAlarmEntity(
                alarmId = alarmData.alarmId,
                alarmTime = alarmData.alarmTime,
                alarmMonday = alarmData.alarmRepeatMonday,
                alarmTuesday = alarmData.alarmRepeatTuesday,
                alarmWednesday = alarmData.alarmRepeatWednesday,
                alarmThursday = alarmData.alarmRepeatThursday,
                alarmFriday = alarmData.alarmRepeatFriday,
                alarmSaturday = alarmData.alarmRepeatSaturday,
                alarmSunday = alarmData.alarmRepeatSunday,
                planId = alarmData.planId
            )
        }
    }
}