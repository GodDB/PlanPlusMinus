package com.example.todoplusminus.repository

import androidx.lifecycle.LiveData
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanMemo
import com.example.todoplusminus.entities.PlanProject
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IPlannerRepository {

    suspend fun refreshPlannerData(date : LocalDate)

    fun getAllPlanProject(): Flow<PlanProject>

    fun getAllPlanMemo() : Flow<MutableList<PlanMemo>>

    fun getAllPlanDataByDate(date: LocalDate): Flow<MutableList<PlanData>>

    fun getPlanProjectById(id: String): Flow<PlanProject>

    fun getLastestIndex(): Flow<Int?>

    fun getMemoByDate(date: LocalDate): Flow<PlanMemo>

    suspend fun deleteMemoByDate(date : LocalDate)

    suspend fun deletePlannerDataById(id: String)

    suspend fun insertPlannerData(planData: PlanData)

    suspend fun updatePlannerDataList(dataList: List<PlanData>)

    suspend fun updatePlannerData(data: PlanData)

    suspend fun getPlannerDataById(id: String): PlanData

    suspend fun deleteAndUpdateAll(deleteTarget: PlanData, updateTarget: List<PlanData>)

    suspend fun updateTitleBgById(id: String, title: String, bgColor: Int)

    suspend fun updatePlanMemo(memo: PlanMemo)

    suspend fun insertPlanMemo(memo: PlanMemo)


}