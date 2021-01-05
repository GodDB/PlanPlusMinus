package com.example.todoplusminus.data.source.local

import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.data.entities.PlanAlarmData
import com.example.todoplusminus.data.entities.PlanData
import com.example.todoplusminus.data.entities.PlanMemo
import com.example.todoplusminus.db.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ILocalDataSource {

    fun getAllPlannerData() : Flow<MutableList<PlanData>>

    fun getAllPlanMemo() : Flow<MutableList<PlanMemo>>

    fun getAllPlannerDataByDate(date : LocalDate) : Flow<MutableList<PlanData>>

    fun getAllPlannerDataById(id : BaseID) : Flow<List<PlanData>>

    fun getLastestIndex() : Flow<Int?>

    fun getMemoByDate(date : LocalDate) : Flow<PlanMemo>

    fun getAllAlarmData(planId : BaseID) : Flow<List<PlannerItemAlarm>>

    fun getAlarmData(alarmId : Int) : Flow<PlannerItemAlarm>

    fun getLatestAlarmId() : Flow<Int?>

    fun getPlannerTitle(planId : BaseID) : Flow<String>

    suspend fun insertAlarmData(alarmData: PlannerAlarmEntity)

    suspend fun updateAlarmData(alarmData: PlannerAlarmEntity)

    suspend fun deleteAlarmDataById(alarmId : Int)

    suspend fun deleteMemoByDate(data : LocalDate)

    suspend fun deleteAllData()

    suspend fun deletePlannerDataById(id : BaseID)

    suspend fun insertPlannerData(planData: PlanData)

    suspend fun updatePlannerDataList(dataList : List<PlanData>)

    suspend fun updatePlannerData(data : PlanData)

    suspend fun deleteAndUpdateAll(deleteTarget : PlanData, updateTarget : List<PlanData>)

    suspend fun getPlannerDataById(id : BaseID) : PlanData

    suspend fun getAllPlanItem() : List<PlannerItemEntity>

    suspend fun getAllPlanInfoByDate(date : LocalDate) : List<PlannerInfoEntity>

    suspend fun updateTitleBgById(id : BaseID, title : String, bgColor : Int)

    suspend fun updatePlanMemo(memo : PlanMemo)

    suspend fun insertPlanMemo(memo : PlanMemo)

    suspend fun insertPlanInfo(info : PlannerInfoEntity)
}

class PlannerMapper() {

    fun planDataToRoomEntity(planData: PlanData) : PlannerItemInfoEntity {
        val item = PlannerItemEntity(planData.id, planData.title, planData.bgColor, planData.index)
        val info = PlannerInfoEntity(planData.infoId, planData.date, planData.count, planData.id)
        return PlannerItemInfoEntity(item, info)
    }

    fun planDataListToRoomEntityList(planDataList : List<PlanData>) : List<PlannerItemInfoEntity>{
        val resultList = mutableListOf<PlannerItemInfoEntity>()
        planDataList.forEach {
            resultList.add(planDataToRoomEntity(it))
        }
        return resultList
    }
}