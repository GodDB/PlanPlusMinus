package com.example.todoplusminus.repository

import androidx.lifecycle.LiveData
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanMemo
import java.time.LocalDate

interface IPlannerRepository {
    fun getAllPlannerData(): LiveData<MutableList<PlanData>>

    suspend fun getAllPlanDataByDate(date: LocalDate): LiveData<MutableList<PlanData>>

    fun getAllPlanDataById(id: String): List<PlanData>

    fun getLastIndex(): Int

    fun getMemoByDate(date: LocalDate): LiveData<PlanMemo>

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