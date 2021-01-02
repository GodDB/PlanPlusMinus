package com.example.todoplusminus.data.repository

import com.example.todoplusminus.data.entities.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IPlannerRepository {

    suspend fun refreshPlannerData(date : LocalDate)

    fun getAllPlanProject(): Flow<PlanProject>

    fun getAllPlanMemo() : Flow<MutableList<PlanMemo>>

    fun getAllPlanDataByDate(date: LocalDate): Flow<MutableList<PlanData>>

    fun getPlanProjectById(id: BaseID): Flow<PlanProject>

    fun getLastestIndex(): Flow<Int?>

    fun getMemoByDate(date: LocalDate): Flow<PlanMemo>

    fun getAllAlarmData(planId : BaseID) : Flow<List<PlanAlarmData>>

    fun getAlarmData(alarmId : Int) : Flow<PlanAlarmData>

    suspend fun insertAlarmData(alarmData: PlanAlarmData)

    suspend fun updateAlarmData(alarmData: PlanAlarmData)

    suspend fun deleteAlarmDataById(alarmId : Int)

    suspend fun deleteMemoByDate(date : LocalDate)

    suspend fun deletePlannerDataById(id: BaseID)

    suspend fun insertPlannerData(planData: PlanData)

    suspend fun updatePlannerDataList(dataList: List<PlanData>)

    suspend fun updatePlannerData(data: PlanData)

    suspend fun getPlannerDataById(id: BaseID): PlanData

    suspend fun deleteAndUpdateAll(deleteTarget: PlanData, updateTarget: List<PlanData>)

    suspend fun updateTitleBgById(id: BaseID, title: String, bgColor: Int)

    suspend fun updatePlanMemo(memo: PlanMemo)

    suspend fun insertPlanMemo(memo: PlanMemo)


}